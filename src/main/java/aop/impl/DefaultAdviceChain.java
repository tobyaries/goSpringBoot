package main.java.aop.impl;

import main.java.aop.interfaces.Advice;
import main.java.aop.interfaces.AdviceChain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * DefaultAdviceChain, a default implementation of the AdviceChain interface.
 * This class manages a list of advices and invokes them in order,
 * eventually invoking the target method.
 */
public class DefaultAdviceChain implements AdviceChain {

    private List<Advice> advices; // The list of advices to be applied
    private int adviceIndex = 0; // The index of the current advice in the list

    /**
     * Constructor.
     *
     * @param advices The list of advices to be applied.
     */
    public DefaultAdviceChain(List<Advice> advices) {
        this.advices = advices;
    }

    /**
     * Invokes the next advice in the chain or the target method.
     *
     * @param target The target object.
     * @param method The method being invoked.
     * @param args   The arguments passed to the method.
     * @return The result returned by the method.
     * @throws Throwable If an exception occurs during the method invocation.
     */
    @Override
    public Object invokeNext(Object target, Method method, Object[] args) throws Throwable {
        // Check if there are more advices to be invoked
        if (adviceIndex < advices.size()) {
            // Get the current advice and increment the index
            Advice advice = advices.get(adviceIndex++);
            // Invoke the around advice of the current advice
            return advice.around(target, method, args, this);
        } else {
            // If all advices have been invoked, invoke the target method
            try {
                // Invoke the target method using reflection
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                // If the target method throws an exception, rethrow it
                throw e.getTargetException();
            }
        }
    }
}