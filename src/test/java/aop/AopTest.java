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

import main.java.aop.interfaces.Advice;
import main.java.aop.impl.LoggerAdvice;
import main.java.aop.proxy.ProxyFactory;
import main.java.beans.MyService;
import main.java.beans.MyServiceImpl;
import org.junit.jupiter.api.Test;

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
        // Create a proxy object
        MyService proxy = (MyService) ProxyFactory.getProxy(target, advice); // 将强制类型转换保持为MyService

        // Test normal method invocation
        proxy.doSomething();

        // Test method invocation with return value
        String result = proxy.doSomethingWithResult();
        assertEquals("Result", result);

        // Test method invocation with exception
        assertThrows(RuntimeException.class, proxy::doSomethingWithException);
    }
}