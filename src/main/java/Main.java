import aop.interfaces.Advice;
import aop.proxy.ProxyFactory;
import beans.A;
import beans.B;
import beans.Dependency;
import beans.Service;
import beans.advices.LoggerAdvice;
import beans.advices.TransactionAdvice;
import beans.users.Register;
import beans.users.UserRegister;
import core.SimpleIOC;
import enums.InjectionType;
import enums.ScopeType;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create SimpleIOC container instance
        SimpleIOC ioc = new SimpleIOC();

        // Register singleton Bean (constructor injection)
        ioc.registerBean("dependency", Dependency.class, InjectionType.CONSTRUCTOR, ScopeType.SINGLETON, null, null);
        ioc.registerBean("service", Service.class, InjectionType.CONSTRUCTOR, ScopeType.SINGLETON, null, null);

        // Get singleton Bean instance
        Service singletonService1 = (Service) ioc.getBean("service");
        Service singletonService2 = (Service) ioc.getBean("service");
        singletonService1.hello();
        System.out.println("singletonService1 == singletonService2: " + (singletonService1 == singletonService2));

        // Register prototype Bean (setter injection)
        ioc.registerBean("prototypeService", Service.class, InjectionType.SETTER, ScopeType.PROTOTYPE, null, null);

        // Get prototype Bean instance
        Service prototypeService1 = (Service) ioc.getBean("prototypeService");
        Dependency dependency = (Dependency) ioc.getBean("dependency");
        prototypeService1.setDependency(dependency);
        prototypeService1.hello();

        Service prototypeService2 = (Service) ioc.getBean("prototypeService");
        System.out.println("prototypeService1 == prototypeService2: " + (prototypeService1 == prototypeService2));

        // Register circular dependency Beans
        ioc.registerBean("a", A.class, InjectionType.SETTER, ScopeType.SINGLETON, "init", "destroy");
        ioc.registerBean("b", B.class, InjectionType.SETTER, ScopeType.SINGLETON, null, null);

        // Get circular dependency Bean instances
        A a = (A) ioc.getBean("a");
        B b = (B) ioc.getBean("b");

        // Verify circular dependency injection
        System.out.println("a.b:" + a.getB());
        System.out.println("b.a:" + b.getA());

        // Get non-existent Bean
        try {
            ioc.getBean("nonExistentBean");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        UserRegister userRegister = new UserRegister();
        Advice loggerAdvice = new LoggerAdvice();
        Advice transactionAdvice = new TransactionAdvice();
        List<Advice> advices = new ArrayList<>();
        advices.add(loggerAdvice);
        advices.add(transactionAdvice);
        Register proxy = (Register) ProxyFactory.getProxy(userRegister, advices);
        String result = proxy.registerUser("ethan", "password");

        // Destroy singleton Beans
        ioc.destroySingletons();
    }
}