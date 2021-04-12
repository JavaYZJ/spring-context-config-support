package red.honey.spring.context.support.reflect;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import red.honey.spring.context.support.annotation.FieldRefresh;
import red.honey.spring.context.support.event.ApolloReflectEvent;
import red.honey.spring.context.support.event.NacosReflectEvent;
import red.honey.spring.context.support.factory.TargetClassFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yangzhijie
 */
public class RefreshInjectedProcessor implements ApplicationContextAware {

    private static Lock NACOS_LOCK = new ReentrantLock();
    private static Lock APOLLO_LCK = new ReentrantLock();
    private Logger log = LoggerFactory.getLogger(RefreshInjectedProcessor.class);
    private ApplicationContext applicationContext;
    private NacosConfigConverter<Map<String, String>> nacosConfigConverter;
    private volatile Map<String, String> configInfo;

    public RefreshInjectedProcessor() {
    }

    public RefreshInjectedProcessor(NacosConfigConverter<Map<String, String>> nacosConfigConverter) {
        this.nacosConfigConverter = nacosConfigConverter;
    }

    @Order
    @EventListener
    public void configChange(EnvironmentChangeEvent event) {
        Set<String> keys = event.getKeys();
        ConcurrentHashSet<Class<?>> classes = TargetClassFactory.factory;
        doRefresh(keys, classes);
    }

    /**
     * NACOS 监听反射通知
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(NacosReflectEvent.class)
    public void nacosReflect(NacosReflectEvent reflectEvent) {
        String configInfo = reflectEvent.getConfigInfo();
        this.configInfo = nacosConfigConverter.convert(configInfo);
    }

    /**
     * APOLLO 监听反射通知
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(ApolloReflectEvent.class)
    public void apolloReflect(ApolloReflectEvent reflectEvent) {
        ConfigChangeEvent changeEvent = reflectEvent.getChangeEvent();
        ConcurrentHashSet<Class<?>> classes = TargetClassFactory.factory;
        APOLLO_LCK.lock();
        try {

            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                for (Class<?> clazz : classes) {
                    doRefresh(clazz, change.getPropertyName());
                }
            }
        } catch (Exception e) {
            log.error("APOLLO: Received the changeKeys for refreshing the Dependency injection", e);
        } finally {
            APOLLO_LCK.unlock();
        }
    }


    private void doRefresh(Set<String> keys, ConcurrentHashSet<Class<?>> classes) {
        NACOS_LOCK.lock();
        try {
            for (String changeKey : keys) {
                for (Class<?> clazz : classes) {
                    doRefresh(clazz, changeKey);
                }
            }
        } catch (Exception e) {
            log.error("NACOS: Received the changeKeys for refreshing the Dependency injection", e);
        } finally {
            NACOS_LOCK.unlock();
        }

    }

    private void doRefresh(Class<?> clazz, String changeKey) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldRefresh.class)) {
                field.setAccessible(true);
                FieldRefresh refresh = field.getAnnotation(FieldRefresh.class);
                String key = refresh.name();
                if (changeKey.equals(key)) {
                    String changeValue = configInfo.get(changeKey);
                    Object bean = applicationContext.getBean(changeValue);
                    Object curObject = applicationContext.getBean(clazz);
                    field.set(curObject, bean);
                }
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
