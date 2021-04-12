package red.honey.spring.context.support.converter;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author yangzhijie
 */
public class PropertiesNacosConfigConverter implements NacosConfigConverter<Map<String, String>> {

    private final static Logger log = LoggerFactory.getLogger(PropertiesNacosConfigConverter.class);


    /**
     * can convert to be target type or not.
     *
     * @param targetType the type of target
     * @return If can , return <code>true</code>, or <code>false</code>
     */
    @Override
    public boolean canConvert(Class<Map<String, String>> targetType) {
        return targetType.isInstance(new HashMap<>());
    }

    /**
     * convert the Naocs's config of type S to target type T.
     *
     * @param config the Naocs's config to convert, which must be an instance of S (never {@code null})
     * @return the converted object, which must be an instance of T (potentially {@code null})
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> convert(String config) {
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(config.getBytes()));
        } catch (IOException e) {
            log.error("PropertiesNacosConfigConverter load config convert to Properties failed:", e);
        }
        return new HashMap<String, String>((Map) properties);
    }


}
