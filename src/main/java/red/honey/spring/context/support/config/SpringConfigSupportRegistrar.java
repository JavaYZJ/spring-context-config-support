package red.honey.spring.context.support.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import red.honey.spring.context.support.annotation.EnableHoneySpringSupport;
import red.honey.spring.context.support.commom.ConfigCenterType;
import red.honey.spring.context.support.entity.SpringConfig;

import java.util.Collections;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

/**
 * @author yangzhijie
 */
public class SpringConfigSupportRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        Set<String> packagesToScan = Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
        SpringConfig springConfig = buildSpringConfig(metadata);
        registerSpringConfig(springConfig, registry);
        registerRefreshInjectedAnnotationBeanPostProcessor(packagesToScan, registry);

    }

    private void registerSpringConfig(SpringConfig springConfig, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = rootBeanDefinition(springConfig.getClass());
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        builder.addConstructorArgValue(springConfig);

        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }

    private void registerRefreshInjectedAnnotationBeanPostProcessor(Set<String> packagesToScan, BeanDefinitionRegistry registry) {

        BeanDefinitionBuilder builder = rootBeanDefinition(RefreshInjectedAnnotationBeanPostProcessor.class);
        builder.addConstructorArgValue(packagesToScan);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);

    }

    private SpringConfig buildSpringConfig(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(EnableHoneySpringSupport.class.getName()));
        String groupId = attributes.getString("groupId");
        ConfigType type = attributes.getEnum("type");
        Class<? extends NacosConfigConverter> converter = attributes.getClass("converter");
        ConfigCenterType centerType = attributes.getEnum("centerType");

        SpringConfig springConfig = new SpringConfig();
        springConfig.setGroupId(groupId);
        springConfig.setType(type);
        springConfig.setConverter(converter);
        springConfig.setCenterType(centerType);
        return springConfig;
    }
}
