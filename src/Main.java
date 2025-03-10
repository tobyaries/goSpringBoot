/*
1. 简化依赖查找： 目前，我们假设依赖的 Bean 的名称与其类型相同。在实际应用中，我们需要更灵活的方式来查找依赖的 Bean，例如，使用配置文件或注解来指定依赖的 Bean 的名称。
2. 支持多个构造函数： 目前，我们只使用 Bean 的第一个构造函数。在实际应用中，我们需要支持多个构造函数，并选择合适的构造函数来创建 Bean 的实例。
3. 循环依赖处理： 目前，我们的 IOC 容器没有处理循环依赖。如果存在循环依赖，则会导致栈溢出。我们需要添加循环依赖检测和处理机制。
4. Bean 的作用域： 目前，我们的 IOC 容器只支持单例 Bean。我们需要添加对原型 Bean 的支持。
5. 配置文件或注解支持： 目前，我们使用硬编码的方式来注册 Bean。在实际应用中，我们需要使用配置文件（例如 XML 或 JSON）或注解来配置 Bean。
6. Setter 注入： 除了构造器注入，我们还需要支持 Setter 注入。
7. 生命周期管理： 我们需要添加 Bean 的生命周期管理，例如，在 Bean 创建后调用初始化方法，在 Bean 销毁前调用销毁方法。
8. AOP 支持： 为了支持面向切面编程（AOP），我们需要添加 AOP 支持。
*/


public class Main {
    public static void main(String[] args) throws Exception {
        // 创建 SimpleIOC 容器实例
        SimpleIOC ioc = new SimpleIOC();

        // 注册 Dependency 类的 Bean 定义（构造器注入）
        ioc.registerBean("dependency", Dependency.class, InjectionType.CONSTRUCTOR);

        // 注册 Service 类的 Bean 定义（构造器注入）
        ioc.registerBean("service", Service.class, InjectionType.CONSTRUCTOR);

        // 获取 Service 类的 Bean 实例 (构造器注入)
        Service service1 = (Service) ioc.getBean("service");
        service1.hello();

        // 创建新的 Service 实例 (Setter 注入)
        Service service2 = (Service) ioc.createBean("service", Service.class, InjectionType.SETTER);
        Dependency dependency = (Dependency) ioc.getBean("dependency");
        service2.setDependency(dependency);
        service2.hello();

       /* // 注册 B 类的 Bean 定义（构造器注入）
        ioc.registerBean("b", B.class, InjectionType.CONSTRUCTOR);

        // 注册 A 类的 Bean 定义（构造器注入）
        ioc.registerBean("a", A.class, InjectionType.CONSTRUCTOR);

        // 获取 A 类的 Bean 实例
        A a = (A) ioc.getBean("a");

        // 获取 B 类的 Bean 实例
        B b = (B) ioc.getBean("b");

        // 验证循环依赖是否正确注入
        System.out.println("a.b:" + a.getB());
        System.out.println("b.a:" + b.getA());*/
    }
}
