package core.ioc;

import core.enums.InjectionType;
import core.enums.ScopeType;

import java.util.Map;

/**
 * Bean definition class, used to store Bean metadata
 */
public class BeanDefinition {
    private String id; // Bean ID
    private Class<?> clazz; // Class object of the Bean
    private InjectionType injectionType; // Injection type of the Bean
    private ScopeType scope; // Scope of the Bean
    private String initMethodName; // Initialization method name of the Bean
    private String destroyMethodName; // Destruction method name of the Bean
    private Map<String, String> propertyValues; // Property values for dependency injection

    public BeanDefinition() {
    }

    public BeanDefinition(Class<?> clazz, InjectionType injectionType, ScopeType scope, String initMethodName, String destroyMethodName) {
        this.clazz = clazz;
        this.injectionType = injectionType;
        this.scope = scope;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setBeanClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    public InjectionType getInjectionType() {
        return injectionType;
    }

    public void setInjectionType(InjectionType injectionType) {
        this.injectionType = injectionType;
    }

    public ScopeType getScope() {
        return scope;
    }

    public void setScope(ScopeType scope) {
        this.scope = scope;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public Map<String, String> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(Map<String, String> propertyValues) {
        this.propertyValues = propertyValues;
    }
}