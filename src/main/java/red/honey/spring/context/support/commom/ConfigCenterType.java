package red.honey.spring.context.support.commom;

/**
 * @author yangzhijie
 */
public enum ConfigCenterType {

    /**
     * nacos config center
     */
    NACOS("nacos"),

    /**
     * apollo config center
     */
    APOLLO("apollo");


    private String type;

    ConfigCenterType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
