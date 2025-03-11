package main.java.core;

import main.java.enums.InjectionType;
import main.java.enums.ScopeType;
import main.java.utils.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleIOC {

    /**
     * 用于存储 Bean 的实例，key 是 Bean 的名称，value 是 Bean 的实例
     */
    private Map<String, Object> beanMap = new HashMap<>();

    /**
     * 用于存储 Bean 的定义，key 是 Bean 的名称，value 是 BeanDefinition 对象
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 用于存储正在创建的 Bean 的名称，用于检测循环依赖
     */
    private Set<String> creatingBeans = new HashSet<>();

    /**
     * 注册 Bean 的定义
     *
     * @param name          Bean 的名称
     * @param clazz         Bean 的 Class 对象
     * @param injectionType Bean 的注入类型
     */
    public void registerBean(String name, Class<?> clazz, InjectionType injectionType, ScopeType scope) {
        beanDefinitionMap.put(name, new BeanDefinition(clazz, injectionType, scope));
    }

    /**
     * 从容器中获取 Bean 的实例
     *
     * @param name Bean 的名称
     * @return Bean 的实例
     * @throws IllegalArgumentException 如果没有找到指定名称的 Bean
     */
    public Object getBean(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("No bean named " + name + " is registered");
        }

        ScopeType scope = beanDefinition.getScope();
        if (ScopeType.SINGLETON.equals(scope)) {
            // 单例 Bean
            Object bean = beanMap.get(name);
            if (bean == null) {
                try {
                    bean = createBean(name, beanDefinition.getClazz(), beanDefinition.getInjectionType());
                    beanMap.put(name, bean);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return bean;
        } else if (ScopeType.PROTOTYPE.equals(scope)) {
            // 原型 Bean
            try {
                return createBean(name, beanDefinition.getClazz(), beanDefinition.getInjectionType());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // 未知作用域
            throw new IllegalArgumentException("Unknown scope: " + scope);
        }
    }

    /**
     * 创建 Bean 的实例，并进行依赖注入
     *
     * @param name          Bean 的名称
     * @param clazz         Bean 的类
     * @param injectionType Bean 的注入类型
     * @return Bean 的实例
     * @throws Exception 如果创建 Bean 的实例失败
     */
    public Object createBean(String name, Class<?> clazz, InjectionType injectionType) throws Exception {
        // 检测循环依赖：如果当前 Bean 正在创建，则抛出异常
        if (creatingBeans.contains(name)) {
            throw new RuntimeException("Circular dependency detected: " + name);
        }
        // 将当前 Bean 的名称添加到正在创建的集合中
        creatingBeans.add(name);

        try {
            Object bean;
            // 根据注入类型选择注入方式
            if (injectionType == InjectionType.CONSTRUCTOR) {
                // 构造器注入
                Constructor<?>[] constructors = clazz.getConstructors();
                if (constructors.length == 0) {
                    // 如果没有构造函数，则使用默认构造函数
                    bean = clazz.getDeclaredConstructor().newInstance();
                } else {
                    // 如果有构造函数，则使用第一个构造函数
                    Constructor<?> constructor = constructors[0];
                    // 获取构造函数的参数
                    Parameter[] parameters = constructor.getParameters();
                    // 用于存储依赖的 Bean 的实例
                    Object[] dependencies = new Object[parameters.length];
                    // 遍历构造函数的参数，获取依赖的 Bean 的实例
                    for (int i = 0; i < parameters.length; i++) {
                        Class<?> parameterType = parameters[i].getType();
                        // 根据类名生成 Bean 的名称
                        String dependencyName = Utils.lowerCaseFirstLetter(parameterType.getSimpleName());
                        // 获取依赖的 Bean 的实例
                        dependencies[i] = getBean(dependencyName);
                    }
                    // 使用构造函数和依赖的 Bean 的实例创建 Bean 的实例
                    bean = constructor.newInstance(dependencies);
                }
            } else {
                // Setter 注入
                bean = clazz.getDeclaredConstructor().newInstance();
            }

            /* ---  此时 bean 已经被创建 ---*/

            // Setter 注入
            // 即使使用构造器注入，也会执行setter注入。因为构造器注入之后可能会有可选参数需要setter注入。
            if (injectionType == InjectionType.SETTER || injectionType == InjectionType.CONSTRUCTOR) {
                // 获取 Bean 的所有方法
                Method[] methods = clazz.getMethods();
                // 遍历方法，找到 Setter 方法
                for (Method method : methods) {
                    if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                        // 获取 Setter 方法的参数类型
                        Class<?> parameterType = method.getParameterTypes()[0];
                        // 根据类名生成依赖的 Bean 的名称
                        String dependencyName = Utils.lowerCaseFirstLetter(parameterType.getSimpleName());
                        // 获取依赖的 Bean 的实例
                        Object dependency = getBean(dependencyName);
                        // 调用 Setter 方法，注入依赖
                        method.invoke(bean, dependency);
                    }
                }
            }
            return bean;
        } finally {
            // 从正在创建的集合中移除当前 Bean 的名称
            creatingBeans.remove(name);
        }
    }

    /**
     * Bean 定义类，用于存储 Bean 的 Class 对象和注入类型
     */
    private static class BeanDefinition {
        private Class<?> clazz;
        private InjectionType injectionType;
        private ScopeType scope;

        public BeanDefinition(Class<?> clazz, InjectionType injectionType, ScopeType scope) {
            // 保存对象的clazz
            this.clazz = clazz;
            // 注入类型 是构造器注入还是setter注入
            this.injectionType = injectionType;
            // 作用域字段 是单例还是原型
            this.scope = scope;
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
    }
}