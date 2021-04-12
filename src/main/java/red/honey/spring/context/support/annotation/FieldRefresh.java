package red.honey.spring.context.support.annotation;

import java.lang.annotation.*;

/**
 * @author yangzhijie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface FieldRefresh {


    /**
     * the config center key
     *
     * @return ""
     */
    String name() default "";

}
