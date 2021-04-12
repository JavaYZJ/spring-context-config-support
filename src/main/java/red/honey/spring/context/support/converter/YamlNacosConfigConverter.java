package red.honey.spring.context.support.converter;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangzhijie
 */
public class YamlNacosConfigConverter implements NacosConfigConverter<Map<String, String>> {

    private final static Logger log = LoggerFactory.getLogger(YamlNacosConfigConverter.class);

    @Override
    public boolean canConvert(Class targetType) {
        return targetType.isInstance(new HashMap<>());
    }


    @Override
    public Map<String, String> convert(String config) {
        Yaml yaml = new Yaml();
        Map<String, String> map = new HashMap<>();
        try {
            map = yaml.load(config);
        } catch (Exception e) {
            log.error("YamlNacosConfigConverter load config convert to Yaml failed:", e);
        }
        return map;
    }
}
