package red.honey.spring.context.support.factory;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;

/**
 * @author yangzhijie
 */
public class TargetClassFactory {

    public static ConcurrentHashSet<Class<?>> factory = new ConcurrentHashSet<>();

    public static void addClass(Class<?> clazz) {
        factory.add(clazz);
    }

}
