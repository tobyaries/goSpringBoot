package core.ioc;

import core.enums.InjectionType;
import core.enums.ScopeType;

/**
 * Bean definition class, used to store Bean metadata
 */
public class BeanDefinition {
    private Class<?> clazz; // Class object of the Bean
    private InjectionType injectionType; // Injection type of the Bean
    private ScopeType scope; // Scope of the Bean
    private String initMethodName; // Initialization method name of the Bean
    private String destroyMethodName; // Destruction method name of the Bean

    public BeanDefinition(Class<?> clazz, InjectionType injectionType, ScopeType scope, String initMethodName, String destroyMethodName) {
        this.clazz = clazz;
        this.injectionType = injectionType;
        this.scope = scope;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public InjectionType getInjectionType() {
        return injectionType;
    }

    public ScopeType getScope() {
        return scope;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }
}