package red.honey.spring.context.support.listener;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import red.honey.spring.context.support.event.NacosReflectEvent;

import java.util.concurrent.Executor;

/**
 * @author yangzhijie
 */
public class NacosConfigListener implements InitializingBean, ApplicationEventPublisherAware {

    private final static Logger log = LoggerFactory.getLogger(NacosConfigListener.class);

    private ApplicationEventPublisher applicationEventPublisher;

    private NacosConfigManager nacosConfigManager;

    private String dataId;

    private String groupId;


    public NacosConfigListener(NacosConfigManager nacosConfigManager,
                               String dataId, String groupId) {
        this.nacosConfigManager = nacosConfigManager;
        this.dataId = dataId;
        this.groupId = groupId;
    }

    @Override
    public void afterPropertiesSet() throws NacosException {
        nacosConfigManager.getConfigService().addListener(dataId, groupId,
                new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        NacosReflectEvent reflectEvent = new NacosReflectEvent(this, configInfo);
                        applicationEventPublisher.publishEvent(reflectEvent);
                    }
                });
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
