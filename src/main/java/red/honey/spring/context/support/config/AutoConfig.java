package red.honey.spring.context.support.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import red.honey.spring.context.support.entity.SpringConfig;
import red.honey.spring.context.support.listener.NacosConfigListener;
import red.honey.spring.context.support.reflect.RefreshInjectedProcessor;

/**
 * @author yangzhijie
 */
@Configuration
public class AutoConfig {

    private static final String APOLLO = "APOLLO";

    private static final String NACOS = "NACOS";

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Autowired
    private SpringConfig springConfig;

    @Value("${spring.application.name}")
    private String appName;


    @Bean
    public NacosConfigListener nacosConfigListener() {
        String groupId = springConfig.getGroupId();
        ConfigType type = springConfig.getType();
        String dataId = appName + "." + type.getType();
        return new NacosConfigListener(nacosConfigManager, dataId, groupId);

    }


    @Bean
    public RefreshInjectedProcessor refreshInjectedProcessor() throws IllegalAccessException, InstantiationException {
        if (springConfig.getCenterType().getType().equalsIgnoreCase(NACOS)) {
            Class<? extends NacosConfigConverter> converter = springConfig.getConverter();
            NacosConfigConverter nacosConfigConverter = converter.newInstance();
            return new RefreshInjectedProcessor(nacosConfigConverter);
        }
        return new RefreshInjectedProcessor();
    }
}
