package red.honey.spring.context.support.entity;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import red.honey.spring.context.support.commom.ConfigCenterType;

/**
 * @author yangzhijie
 */
public class SpringConfig {

    private String groupId;

    private ConfigType type;

    private Class<? extends NacosConfigConverter> converter;

    private ConfigCenterType centerType;

    public SpringConfig() {
    }

    public SpringConfig(SpringConfig springConfig) {
        this.groupId = springConfig.getGroupId();
        this.type = springConfig.getType();
        this.converter = springConfig.getConverter();
        this.centerType = springConfig.getCenterType();
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ConfigType getType() {
        return type;
    }

    public void setType(ConfigType type) {
        this.type = type;
    }

    public Class<? extends NacosConfigConverter> getConverter() {
        return converter;
    }

    public void setConverter(Class<? extends NacosConfigConverter> converter) {
        this.converter = converter;
    }

    public ConfigCenterType getCenterType() {
        return centerType;
    }

    public void setCenterType(ConfigCenterType centerType) {
        this.centerType = centerType;
    }
}
