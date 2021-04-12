package red.honey.spring.context.support.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author yangzhijie
 */
public abstract class AbstractReflectEvent extends ApplicationEvent {

    private String configInfo;

    public AbstractReflectEvent(Object source, String configInfo) {
        super(source);
        this.configInfo = configInfo;
    }

    public String getConfigInfo() {
        return configInfo;
    }
}
