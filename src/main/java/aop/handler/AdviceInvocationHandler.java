package main.java.aop.handler;

import main.java.aop.impl.DefaultAdviceChain;
import main.java.aop.interfaces.Advice;
import main.java.aop.interfaces.AdviceChain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * AdviceInvocationHandler, handles method invocations on proxy objects.
 */
public class AdviceInvocationHandler implements InvocationHandler {

    private Object target; // The target object
    private Advice advice; // The advice to be applied

    /**
     * Constructor.
     *
     * @param target The target object.
     * @param advice The advice to be applied.
     */
    public AdviceInvocationHandler(Object target, Advice advice) {
        this.target = target;
        this.advice = advice;
    }

    /**
     * Invokes the method on the target object, applying the advice.
     *
     * @param proxy  The proxy object.
     * @param method The method being invoked.
     * @param args   The arguments passed to the method.
     * @return The result returned by the method.
     * @throws Throwable If an exception occurs during the method invocation.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Create an advice chain
        List<Advice> advices = new ArrayList<>();
        advices.add(advice);
        AdviceChain adviceChain = new DefaultAdviceChain(advices);
        // Invoke the advice chain
        return adviceChain.invokeNext(target, method, args);
    }
}