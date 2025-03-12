package main.java.core;

import main.java.enums.InjectionType;
import main.java.utils.Utils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Default Bean factory, responsible for Bean creation and dependency injection
 */
public class DefaultListableBeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap;
    private Map<String, Object> singletonObjects;
    private Map<String, Object> earlySingletonObjects;
    private Map<String, Supplier<?>> singletonFactories;
    private Set<String> creatingBeans = new HashSet<>();
    private BeanProvider beanProvider;


    /**
     * Constructor, receives Bean definition map, singleton cache, and early singleton cache
     *
     * @param beanDefinitionMap   Bean definition map
     * @param singletonObjects    Singleton cache
     * @param earlySingletonObjects Early singleton cache
     * @param singletonFactories  Singleton factories cache
     */
    public DefaultListableBeanFactory(Map<String, BeanDefinition> beanDefinitionMap,
                                      Map<String, Object> singletonObjects,
                                      Map<String, Object> earlySingletonObjects,
                                      Map<String, Supplier<?>> singletonFactories) {
        this.beanDefinitionMap = beanDefinitionMap;
        this.singletonObjects = singletonObjects;
        this.earlySingletonObjects = earlySingletonObjects;
        this.singletonFactories = singletonFactories;
        this.beanProvider = new BeanProvider(beanDefinitionMap, singletonObjects, earlySingletonObjects, singletonFactories, this);
    }

    /**
     * Create Bean instance and perform dependency injection
     *
     * @param name          Bean name
     * @param beanDefinition Bean definition
     * @return Bean instance
     * @throws Exception If Bean instance creation fails
     */
    public Object createBean(String name, BeanDefinition beanDefinition) throws Exception {
        // Check for circular dependency: if the current Bean is being created, throw an exception
        if (creatingBeans.contains(name)) {
            // If it is constructor injection, throw an exception
            if (beanDefinition.getInjectionType() == InjectionType.CONSTRUCTOR) {
                throw new RuntimeException("Circular dependency detected: " + name);
            }
            // If it is setter injection, try to get the early reference from the secondary cache
            Object earlyBean = earlySingletonObjects.get(name);
            if (earlyBean != null) {
                return earlyBean;
            }
        }
        // Add the current Bean name to the set of Beans being created
        creatingBeans.add(name);

        try {
            Object bean;
            // Select injection method based on injection type
            if (beanDefinition.getInjectionType() == InjectionType.CONSTRUCTOR) {
                // Constructor injection
                Constructor<?>[] constructors = beanDefinition.getClazz().getConstructors();
                if (constructors.length == 0) {
                    // If there is no constructor, use the default constructor
                    bean = beanDefinition.getClazz().getDeclaredConstructor().newInstance();
                } else {
                    // If there is a constructor, use the first constructor
                    Constructor<?> constructor = constructors[0];
                    // Get the parameters of the constructor
                    Parameter[] parameters = constructor.getParameters();
                    // Store the instances of dependent Beans
                    Object[] dependencies = new Object[parameters.length];
                    // Iterate through the parameters of the constructor to get the instances of dependent Beans
                    for (int i = 0; i < parameters.length; i++) {
                        Class<?> parameterType = parameters[i].getType();
                        // Generate the Bean name based on the class name
                        String dependencyName = Utils.lowerCaseFirstLetter(parameterType.getSimpleName());
                        // Get the instance of the dependent Bean
                        dependencies[i] = getBean(dependencyName);
                    }
                    // Create the Bean instance using the constructor and the instances of dependent Beans
                    bean = constructor.newInstance(dependencies);
                }
            } else {
                // Setter injection
                bean = beanDefinition.getClazz().getDeclaredConstructor().newInstance();
            }

            /* ---  Bean has been created at this point ---*/
            // Put the Bean's ObjectFactory into the tertiary cache
            singletonFactories.put(name, () -> bean);

            // Put the Bean's early reference into the secondary cache
            earlySingletonObjects.put(name, bean);

            // Setter injection
            // Even if constructor injection is used, setter injection will be performed. Because there may be optional parameters that need setter injection after constructor injection.
            if (beanDefinition.getInjectionType() == InjectionType.SETTER || beanDefinition.getInjectionType() == InjectionType.CONSTRUCTOR) {
                // Get all methods of the Bean
                Method[] methods = beanDefinition.getClazz().getMethods();
                // Iterate through the methods to find Setter methods
                for (Method method : methods) {
                    if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                        // Get the parameter type of the Setter method
                        Class<?> parameterType = method.getParameterTypes()[0];
                        // Generate the name of the dependent Bean based on the class name
                        String dependencyName = Utils.lowerCaseFirstLetter(parameterType.getSimpleName());
                        // Check for circular dependency
                        if (creatingBeans.contains(dependencyName)) {
                            // Try to get from the secondary cache
                            Object earlyDependency = earlySingletonObjects.get(dependencyName);
                            if (earlyDependency != null) {
                                method.invoke(bean, earlyDependency);
                                continue;
                            } else{
                                // If not found in the secondary cache, it means there is a problem with the circular dependency, throw an exception
                                throw new RuntimeException("Circular dependency cannot be resolved for " + name);
                            }
                        }
                        // Get the instance of the dependent Bean
                        Object dependency = getBean(dependencyName);
                        // Call the Setter method to inject the dependency
                        method.invoke(bean, dependency);
                    }
                }
            }
            // Call the initialization method
            invokeInitMethod(bean, beanDefinition.getInitMethodName());
            return bean;
        } finally {
            // Remove the current Bean name from the set of Beans being created
            creatingBeans.remove(name);
        }
    }

    private Object getBean(String name) {
        return beanProvider.getBean(name);
    }

    private void invokeInitMethod(Object bean, String methodName) throws Exception {
        if (methodName != null && !methodName.isEmpty()) {
            Method initMethod = bean.getClass().getMethod(methodName);
            initMethod.invoke(bean);
        }
    }
}