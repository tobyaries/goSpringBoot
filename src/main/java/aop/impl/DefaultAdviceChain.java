package aop.impl;


import aop.interfaces.Advice;
import aop.interfaces.AdviceChain;

import java.lang.reflect.Method;
import java.util.List;

/**
 * DefaultAdviceChain, a default implementation of the AdviceChain interface.
 * Manages the execution of a list of advices in a chain.
 */
public class DefaultAdviceChain implements AdviceChain {
    private Object target;
    private Method method;
    private Object[] args;
    private List<Advice> advices;
    private int adviceIndex = 0;

    /**
     * Constructs a DefaultAdviceChain with the specified target object, method, arguments, and advices.
     *
     * @param target   The target object.
     * @param method   The method being invoked.
     * @param args     The arguments passed to the method.
     * @param advices  The list of advices to apply.
     */
    public DefaultAdviceChain(Object target, Method method, Object[] args, List<Advice> advices) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.advices = advices;
    }

    /**
     * Invokes the next advice in the chain or the target method.
     *
     * @param target The target object.
     * @param method The method being invoked.
     * @param args   The arguments passed to the method.
     * @return The result returned by the method invocation.
     * @throws Throwable If an exception occurs during the method invocation.
     */
    @Override
    public Object invokeNext(Object target, Method method, Object[] args) throws Throwable {
        // Check if there are more advices to execute
        if (adviceIndex < advices.size()) {
            // Get the next advice from the list
            Advice advice = advices.get(adviceIndex++);
            // Invoke the around method of the advice, passing the target, method, arguments, and this advice chain
            return advice.around(target, method, args, this);
        } else {
            // If no more advices, invoke the target method
            return method.invoke(target, args);
        }
    }
}