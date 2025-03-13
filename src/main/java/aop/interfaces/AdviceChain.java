package main.java.aop.interfaces;

import java.lang.reflect.Method;

/**
 * AdviceChain interface, defines the behavior of an advice chain.
 * An advice chain is a list of advices that are executed in order.
 */
public interface AdviceChain {

    /**
     * Invokes the next advice in the chain or the target method.
     *
     * @param target The target object.
     * @param method The method being invoked.
     * @param args   The arguments passed to the method.
     * @return The result returned by the method.
     * @throws Throwable If an exception occurs during the method invocation.
     */
    Object invokeNext(Object target, Method method, Object[] args) throws Throwable;
}