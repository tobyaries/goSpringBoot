package test.java.core;

import main.java.beans.A;
import main.java.beans.B;
import main.java.beans.Dependency;
import main.java.beans.Service;
import main.java.core.SimpleIOC;
import main.java.enums.InjectionType;
import main.java.enums.ScopeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleIOCTest {

    private SimpleIOC ioc;

    @BeforeEach
    void setUp() {
        ioc = new SimpleIOC();
    }

    @Test
    void testSingletonBean() {
        ioc.registerBean("dependency", Dependency.class, InjectionType.CONSTRUCTOR, ScopeType.SINGLETON);
        Dependency dependency1 = (Dependency) ioc.getBean("dependency");
        Dependency dependency2 = (Dependency) ioc.getBean("dependency");
        assertSame(dependency1, dependency2);
    }

    @Test
    void testPrototypeBean() {
        ioc.registerBean("dependency", Dependency.class, InjectionType.CONSTRUCTOR, ScopeType.SINGLETON);
        ioc.registerBean("service", Service.class, InjectionType.CONSTRUCTOR, ScopeType.PROTOTYPE);
        Service service1 = (Service) ioc.getBean("service");
        Service service2 = (Service) ioc.getBean("service");
        assertNotSame(service1, service2);
    }

    @Test
    void testCircularDependency() {
        ioc.registerBean("a", A.class, InjectionType.SETTER, ScopeType.SINGLETON);
        ioc.registerBean("b", B.class, InjectionType.SETTER, ScopeType.SINGLETON);
        A a = (A) ioc.getBean("a");
        B b = (B) ioc.getBean("b");
        assertSame(b, a.getB());
        assertSame(a, b.getA());
    }

    @Test
    void testNonExistentBean() {
        assertThrows(IllegalArgumentException.class, () -> ioc.getBean("nonExistentBean"));
    }
}