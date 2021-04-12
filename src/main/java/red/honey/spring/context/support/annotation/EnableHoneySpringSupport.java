package red.honey.spring.context.support.annotation;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import org.springframework.context.annotation.Import;
import red.honey.spring.context.support.commom.ConfigCenterType;
import red.honey.spring.context.support.config.AutoConfig;
import red.honey.spring.context.support.config.SpringConfigSupportRegistrar;
import red.honey.spring.context.support.converter.PropertiesNacosConfigConverter;

import java.lang.annotation.*;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;


/**
 * @author yangzhijie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SpringConfigSupportRegistrar.class, AutoConfig.class})
public @interface EnableHoneySpringSupport {
    /**
     * Nacos groupId .if ConfigCenterType is NACOS
     *
     * @return
     */
    String groupId() default DEFAULT_GROUP;

    /**
     * Nacos Config type.if ConfigCenterType is NACOS
     *
     * @return "properties"
     */
    ConfigType type() default ConfigType.PROPERTIES;

    /**
     * Specify {@link NacosConfigConverter Nacos configuraion convertor} class to convert target type instance.
     *
     * @return The implementation class of {@link NacosConfigConverter}
     */
    Class<? extends NacosConfigConverter> converter() default PropertiesNacosConfigConverter.class;


    /**
     * config center type.
     *
     * @return "nacos"
     */
    ConfigCenterType centerType() default ConfigCenterType.NACOS;
}
