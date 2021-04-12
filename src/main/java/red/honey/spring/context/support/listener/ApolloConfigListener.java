package red.honey.spring.context.support.listener;


import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import red.honey.spring.context.support.event.ApolloReflectEvent;

/**
 * @author yangzhijie
 */
public class ApolloConfigListener implements InitializingBean, ApplicationEventPublisherAware {

    private Logger log = LoggerFactory.getLogger(ApolloConfigListener.class);

    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void afterPropertiesSet() {
        // config instance is singleton for each namespace and is never null
        Config config = ConfigService.getAppConfig();
        config.addChangeListener(changeEvent -> {
            log.info("Changes for namespace " + changeEvent.getNamespace());
            ApolloReflectEvent reflectEvent = new ApolloReflectEvent(this, null, changeEvent);
            this.applicationEventPublisher.publishEvent(reflectEvent);
        });
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
