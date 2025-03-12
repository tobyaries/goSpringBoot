package main.java.core;

import main.java.enums.ScopeType;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Shared Bean provider, responsible for providing Bean instances
 */
public class BeanProvider {

    private Map<String, BeanDefinition> beanDefinitionMap;
    private Map<String, Object> singletonObjects;
    private Map<String, Object> earlySingletonObjects;
    private Map<String, Supplier<?>> singletonFactories;
    private DefaultListableBeanFactory beanFactory;

    public BeanProvider(Map<String, BeanDefinition> beanDefinitionMap,
                        Map<String, Object> singletonObjects,
                        Map<String, Object> earlySingletonObjects,
                        Map<String, Supplier<?>> singletonFactories,
                        DefaultListableBeanFactory beanFactory) {
        this.beanDefinitionMap = beanDefinitionMap;
        this.singletonObjects = singletonObjects;
        this.earlySingletonObjects = earlySingletonObjects;
        this.singletonFactories = singletonFactories;
        this.beanFactory = beanFactory;
    }

    /**
     * Get Bean instance
     *
     * @param name Bean name
     * @return Bean instance
     * @throws IllegalArgumentException if no Bean with the given name is registered
     */
    public Object getBean(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("No bean named " + name + " is registered");
        }
        ScopeType scope = beanDefinition.getScope();
        if (ScopeType.SINGLETON.equals(scope)) {
            // Singleton Bean
            // Try to get the fully created Bean instance from the first-level cache
            Object bean = singletonObjects.get(name);
            if (bean == null) {
                // If not found in the first-level cache, try to get the early-referenced Bean instance from the second-level cache
                bean = getSingletonObjectsFromCache(name);
                // If still not found, it means the Bean is not registered yet
                if (bean == null) {
                    try {
                        // Create the Bean instance using the Bean factory
                        bean = beanFactory.createBean(name, beanDefinition);
                        // Put the fully created Bean instance into the first-level cache
                        singletonObjects.put(name, bean);
                        // Remove the early-referenced Bean instance from the second-level cache
                        earlySingletonObjects.remove(name);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return bean;
        } else if (ScopeType.PROTOTYPE.equals(scope)) {
            // Prototype Bean
            try {
                // Create a new Bean instance using the Bean factory
                return beanFactory.createBean(name, beanDefinition);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // Unknown scope
            throw new IllegalArgumentException("Unknown scope: " + scope);
        }
    }

    Object getSingletonObjectsFromCache(String name) {
        Object bean;
        // Try to get the early-referenced Bean instance from the second-level cache
        bean = earlySingletonObjects.get(name);
        if (bean == null) {
            // If not found in the second-level cache, try to get the ObjectFactory from the third-level cache
            Supplier<?> objectFactory = singletonFactories.get(name);
            if (objectFactory != null) {
                // Create the early-referenced Bean instance using the ObjectFactory
                bean = objectFactory.get();
                // Put the early-referenced Bean instance into the second-level cache
                earlySingletonObjects.put(name, bean);
                // Remove the ObjectFactory from the third-level cache
                singletonFactories.remove(name);
            }
        }
        return bean;
    }
}