package red.honey.spring.context.support.event;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;

/**
 * @author yangzhijie
 */
public class ApolloReflectEvent extends AbstractReflectEvent {

    private ConfigChangeEvent changeEvent;

    public ApolloReflectEvent(Object source, String configInfo) {
        super(source, configInfo);
    }


    public ApolloReflectEvent(Object source, String configInfo, ConfigChangeEvent changeEvent) {
        super(source, configInfo);
        this.changeEvent = changeEvent;
    }

    public ConfigChangeEvent getChangeEvent() {
        return changeEvent;
    }
}
