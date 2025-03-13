package main.java.beans.advices;

import main.java.aop.interfaces.Advice;
import main.java.aop.interfaces.AdviceChain;

import java.lang.reflect.Method;

public class TransactionAdvice implements Advice {
    @Override
    public void before(Object target, Method method, Object[] args) {

    }

    @Override
    public void after(Object target, Method method, Object[] args, Object result) {

    }

    @Override
    public void afterThrowing(Object target, Method method, Object[] args, Throwable throwable) {

    }

    @Override
    public Object around(Object target, Method method, Object[] args, AdviceChain adviceChain) throws Throwable {
        System.out.println("Transaction: Start transaction before " + method.getName());
        Object result = adviceChain.invokeNext(target, method, args);
        System.out.println("Transaction: Commit transaction after " + method.getName());
        return result;
    }
}
