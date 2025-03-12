package main.java.core;

import main.java.enums.InjectionType;
import main.java.enums.ScopeType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Simple IOC container
 */
public class SimpleIOC {

    private Map<String, Object> singletonObjects = new HashMap<>();
    private Map<String, Object> earlySingletonObjects = new HashMap<>();
    private Map<String, Supplier<?>> singletonFactories = new HashMap<>();
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(beanDefinitionMap, singletonObjects, earlySingletonObjects, singletonFactories);
    private BeanProvider beanProvider = new BeanProvider(beanDefinitionMap, singletonObjects, earlySingletonObjects, singletonFactories, beanFactory);

    /**
     * Register Bean definition
     *
     * @param name          Bean name
     * @param clazz         Bean class
     * @param injectionType Bean injection type
     * @param scope         Bean scope
     * @param initMethodName Bean initialization method name
     * @param destroyMethodName Bean destruction method name
     */
    public void registerBean(String name, Class<?> clazz, InjectionType injectionType, ScopeType scope, String initMethodName, String destroyMethodName) {
        beanDefinitionMap.put(name, new BeanDefinition(clazz, injectionType, scope, initMethodName, destroyMethodName));
    }

    /**
     * Get Bean instance from container
     *
     * @param name Bean name
     * @return Bean instance
     * @throws IllegalArgumentException if no Bean with the given name is registered
     */
    public Object getBean(String name) {
        return beanProvider.getBean(name);
    }

    /**
     * Destroy all singleton Beans
     *
     * @throws Exception if Bean destruction fails
     */
    public void destroySingletons() throws Exception {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            // 1. Iterate through beanDefinitionMap to get each Bean's name and definition
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            // 2. Check if the Bean's scope is singleton
            if (beanDefinition.getScope() == ScopeType.SINGLETON) {
                // 3. If it is a singleton Bean, get the Bean instance from the singletonObjects cache
                Object bean = singletonObjects.get(beanName);
                // 4. Check if the Bean instance exists and a destroy method is defined
                if (bean != null && beanDefinition.getDestroyMethodName() != null) {
                    // 5. Get the Bean's destroy method
                    Method destroyMethod = bean.getClass().getMethod(beanDefinition.getDestroyMethodName());
                    // 6. Invoke the Bean's destroy method
                    destroyMethod.invoke(bean);
                }
            }
        }
    }
}