/* This is a workflow graph
   can see it in Mermaid Live Editor: https://mermaid.live/
    graph TD
    A[Client] --> B(ProxyFactory);
    B --> C{Proxy};
    C --> D[AdviceInvocationHandler];
    D --> E[DefaultAdviceChain];
    E --> F[Advice];
    E --> G[TargetObject];
    G --> E;
    E --> D;
    F --> E;
    D --> C;
    C --> A;
*/

package test.java.aop;

import main.java.beans.advices.TransactionAdvice;
import main.java.aop.interfaces.Advice;
import main.java.beans.advices.LoggerAdvice;
import main.java.aop.proxy.ProxyFactory;
import main.java.beans.users.MyService;
import main.java.beans.users.MyServiceImpl;
import main.java.beans.users.Register;
import main.java.beans.users.UserRegister;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * AopTest, a test class for AOP functionality.
 */
public class AopTest {

    /**
     * Tests the AOP proxy functionality.
     * Verifies that the proxy object correctly applies the advice to the target object.
     */
    @Test
    void testAopProxy() {
        // Create a target object
        MyServiceImpl target = new MyServiceImpl();
        // Create an advice object
        Advice advice = new LoggerAdvice();
        // Create a list of advices
        List<Advice> advices = new ArrayList<>();
        advices.add(advice);
        // Create a proxy object
        MyService proxy = (MyService) ProxyFactory.getProxy(target, advices); // 将强制类型转换保持为MyService

        // Test normal method invocation
        proxy.doSomething();

        // Test method invocation with return value
        String result = proxy.doSomethingWithResult();
        assertEquals("Result", result);

        // Test method invocation with exception
        assertThrows(RuntimeException.class, proxy::doSomethingWithException);
    }

    /**
     * Tests the AOP proxy functionality with LoggerAdvice and TransactionAdvice.
     * Verifies that the proxy object correctly applies the advices to the target object.
     */
    @Test
    void testAopProxyWithLoggerAndTransactionAdvices() {
        // Create a target object
        UserRegister userRegister = new UserRegister();

        // Create two advice objects
        Advice loggerAdvice = new LoggerAdvice();
        Advice transactionAdvice = new TransactionAdvice();

        // Create a list of advices
        List<Advice> advices = new ArrayList<>();
        advices.add(loggerAdvice);
        advices.add(transactionAdvice);

        // Create a proxy object
        Register proxy = (Register) ProxyFactory.getProxy(userRegister, advices);

        // Test normal method invocation
        String result = proxy.registerUser("ethan", "password");
        assertEquals("Result", result);
    }
}