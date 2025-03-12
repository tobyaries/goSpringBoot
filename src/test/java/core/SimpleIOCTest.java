package test.java.core;

import main.java.beans.A;
import main.java.beans.B;
import main.java.beans.Dependency;
import main.java.beans.Service;
import main.java.core.SimpleIOC; // Replace OldSimpleIOC with SimpleIOC
import main.java.enums.InjectionType;
import main.java.enums.ScopeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleIOCTest {

    private SimpleIOC ioc; // Replace OldSimpleIOC with SimpleIOC

    @BeforeEach
    void setUp() {
        ioc = new SimpleIOC(); // Replace OldSimpleIOC with SimpleIOC
    }

    @Test
    void testSingletonBean() {
        // Register a singleton Bean
        ioc.registerBean("dependency", Dependency.class, InjectionType.CONSTRUCTOR, ScopeType.SINGLETON, null, null);
        // Get two instances of the singleton Bean
        Dependency dependency1 = (Dependency) ioc.getBean("dependency");
        Dependency dependency2 = (Dependency) ioc.getBean("dependency");
        // Verify that the two instances are the same
        assertSame(dependency1, dependency2);
    }

    @Test
    void testPrototypeBean() {
        // Register a singleton Dependency Bean and a prototype Service Bean
        ioc.registerBean("dependency", Dependency.class, InjectionType.CONSTRUCTOR, ScopeType.SINGLETON, null, null);
        ioc.registerBean("service", Service.class, InjectionType.CONSTRUCTOR, ScopeType.PROTOTYPE, null, null);
        // Get two instances of the prototype Bean
        Service service1 = (Service) ioc.getBean("service");
        Service service2 = (Service) ioc.getBean("service");
        // Verify that the two instances are not the same
        assertNotSame(service1, service2);
    }

    @Test
    void testCircularDependency() {
        // Register two Beans with circular dependencies
        ioc.registerBean("a", A.class, InjectionType.SETTER, ScopeType.SINGLETON, null, null);
        ioc.registerBean("b", B.class, InjectionType.SETTER, ScopeType.SINGLETON, null, null);
        // Get instances of the Beans
        A a = (A) ioc.getBean("a");
        B b = (B) ioc.getBean("b");
        // Verify that the circular dependencies are correctly injected
        assertSame(b, a.getB());
        assertSame(a, b.getA());
    }

    @Test
    void testNonExistentBean() {
        // Verify that an exception is thrown when trying to get a non-existent Bean
        assertThrows(IllegalArgumentException.class, () -> ioc.getBean("nonExistentBean"));
    }

    @Test
    void testInitAndDestroyMethods() throws Exception {
        // Register A and B with init and destroy methods
        ioc.registerBean("a", A.class, InjectionType.SETTER, ScopeType.SINGLETON, "init", "destroy");
        ioc.registerBean("b", B.class, InjectionType.SETTER, ScopeType.SINGLETON, null, null);
        // Get A instance
        A a = (A) ioc.getBean("a");
        // Destroy singletons and verify that no errors are thrown
        ioc.destroySingletons();
    }
}