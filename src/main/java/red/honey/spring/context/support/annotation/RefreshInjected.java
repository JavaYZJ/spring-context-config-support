package red.honey.spring.context.support.annotation;

import java.lang.annotation.*;

/**
 * @author yangzhijie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RefreshInjected {

    /**
     * the fields should be refresh
     *
     * @return {}
     */
    String[] fields() default {};


}
