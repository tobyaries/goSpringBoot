package core.aop.interfaces;

import java.lang.reflect.Method;

/**
 * Advice interface, defines the behavior of an advice.
 * An advice is a piece of code that is executed before, after, or around a method invocation.
 */
public interface Advice {

    /**
     * Method invoked before the target method execution.
     *
     * @param target The target object.
     * @param method The method being invoked.
     * @param args   The arguments passed to the method.
     */
    void before(Object target, Method method, Object[] args);

    /**
     * Method invoked after the target method execution.
     *
     * @param target The target object.
     * @param method The method being invoked.
     * @param args   The arguments passed to the method.
     * @param result The result returned by the method.
     */
    void after(Object target, Method method, Object[] args, Object result);

    /**
     * Method invoked after the target method execution throws an exception.
     *
     * @param target    The target object.
     * @param method    The method being invoked.
     * @param args      The arguments passed to the method.
     * @param throwable The exception thrown by the method.
     */
    void afterThrowing(Object target, Method method, Object[] args, Throwable throwable);

    /**
     * Method invoked around the target method execution.
     * This method can control the execution of the target method and other advices.
     *
     * @param target      The target object.
     * @param method      The method being invoked.
     * @param args        The arguments passed to the method.
     * @param adviceChain The advice chain, used to invoke the next advice or the target method.
     * @return The result returned by the method.
     * @throws Throwable If an exception occurs during the method invocation.
     */
    Object around(Object target, Method method, Object[] args, AdviceChain adviceChain) throws Throwable;
}