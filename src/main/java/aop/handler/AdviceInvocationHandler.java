package aop.handler;

import aop.impl.DefaultAdviceChain;
import aop.interfaces.Advice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * AdviceInvocationHandler, an invocation handler for applying advices to a target object.
 */
public class AdviceInvocationHandler implements InvocationHandler {
    private Object target;
    private List<Advice> advices;

    /**
     * Constructs an AdviceInvocationHandler with the specified target object and advices.
     *
     * @param target   The target object.
     * @param advices  The list of advices to apply.
     */
    public AdviceInvocationHandler(Object target, List<Advice> advices) {
        this.target = target;
        this.advices = advices;
    }

    /**
     * Intercepts method invocations and applies the advices to the target object.
     *
     * @param proxy    The proxy object.
     * @param method   The method being invoked.
     * @param args     The arguments passed to the method.
     * @return         The result returned by the method invocation.
     * @throws Throwable If an exception occurs during the method invocation.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Create an advice chain to manage the execution of advices
        DefaultAdviceChain adviceChain = new DefaultAdviceChain(target, method, args, advices);
        // Invoke the advice chain, passing the target, method, and arguments
        return adviceChain.invokeNext(target, method, args);
    }
}