package main.java.beans.advices;

import main.java.aop.interfaces.Advice;
import main.java.aop.interfaces.AdviceChain;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * LoggerAdvice, simple advice that logs method invocations.
 */
public class LoggerAdvice implements Advice {

    /**
     * Method invoked before the target method execution.
     * Logs the method name.
     *
     * @param target The target object.
     * @param method The method being invoked.
     * @param args   The arguments passed to the method.
     */
    @Override
    public void before(Object target, Method method, Object[] args) {
        System.out.println("Logger: Before " + method.getName() +
                ", arguments: " + Arrays.toString(args));
    }

    /**
     * Method invoked after the target method execution.
     * Logs the method name and the result returned by the method.
     *
     * @param target The target object.
     * @param method The method being invoked.
     * @param args   The arguments passed to the method.
     * @param result The result returned by the method.
     */
    @Override
    public void after(Object target, Method method, Object[] args, Object result) {
        System.out.println("Logger: After " + method.getName() +
                ", result: " + result);
    }

    /**
     * Method invoked after the target method execution throws an exception.
     * Logs the method name and the exception message.
     *
     * @param target    The target object.
     * @param method    The method being invoked.
     * @param args      The arguments passed to the method.
     * @param throwable The exception thrown by the method.
     */
    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable throwable) {
        System.out.println("Logger: Exception in " + method.getName() +
                ": " + throwable.getMessage());
    }

    /**
     * Method invoked around the target method execution.
     * Logs the method name before and after the method invocation.
     * Controls the execution of the target method and other advices.
     *
     * @param target      The target object.
     * @param method      The method being invoked.
     * @param args        The arguments passed to the method.
     * @param adviceChain The advice chain, used to invoke the next advice or the target method.
     * @return The result returned by the method.
     * @throws Throwable If an exception occurs during the method invocation.
     */
    @Override
    public Object around(Object target, Method method, Object[] args, AdviceChain adviceChain) throws Throwable {
        System.out.println("Logger: Start logger before " + method.getName());
        Object result = adviceChain.invokeNext(target, method, args);
        System.out.println("Logger: Finish logger after " + method.getName());
        return result;
    }
}