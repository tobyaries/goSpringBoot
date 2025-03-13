package main.java.aop.proxy;

import main.java.aop.handler.AdviceInvocationHandler;
import main.java.aop.interfaces.Advice;

import java.lang.reflect.Proxy;

/**
 * ProxyFactory, used to create proxy objects.
 */
public class ProxyFactory {

    /**
     * Creates a proxy object for the given target object and advice.
     *
     * @param target The target object.
     * @param advice The advice to be applied to the target object.
     * @return The proxy object.
     */
    public static Object getProxy(Object target, Advice advice) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AdviceInvocationHandler(target, advice)
        );
    }
}