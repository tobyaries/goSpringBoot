package main.java.aop.proxy;

import main.java.aop.impl.AdviceInvocationHandler;
import main.java.aop.interfaces.Advice;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * ProxyFactory, used to create proxy objects.
 */
public class ProxyFactory {

    /**
     * Creates a proxy object for the given target object and advice.
     *
     * @param advices The advice to be applied to the target object list.
     * @param target The target object.
     * @return The proxy object.
     */
    public static Object getProxy(Object target, List<Advice> advices) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AdviceInvocationHandler(target, advices)
        );
    }
}