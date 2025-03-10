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
     * 用于存储 Bean 的定义（Class 对象），key 是 Bean 的名称，value 是 Bean 的 Class 对象
     *
     * * 改动原因：为了支持循环依赖，我们需要先注册 Bean 的定义，然后再创建 Bean 的实例。
     */
    private Map<String, Class<?>> beanDefinitionMap = new HashMap<>();

    /**
     * 用于存储正在创建的 Bean 的名称，用于检测循环依赖
     */
    private Set<String> creatingBeans = new HashSet<>();

    /**
     * 注册 Bean 的定义（Class 对象）
     *
     * @param name  Bean 的名称
     * @param clazz Bean 的 Class 对象
     *
     * 改动原因：为了支持循环依赖，我们需要先注册 Bean 的定义，然后再创建 Bean 的实例。
     */
    public void registerBean(String name, Class<?> clazz) {
        beanDefinitionMap.put(name, clazz);
    }

    /**
     * 从容器中获取 Bean 的实例
     *
     * @param name Bean 的名称
     * @return Bean 的实例
     * @throws IllegalArgumentException 如果没有找到指定名称的 Bean
     *
     * 改动原因：为了支持循环依赖，我们需要先检查 Bean 是否已经创建，如果未创建，则调用 createBean 方法创建 Bean 的实例。
     */
    public Object getBean(String name) {
        // 先检查 Bean 是否已经创建
        Object bean = beanMap.get(name);
        if (bean == null) {
            // 如果 Bean 未创建，则获取 Bean 的定义
            Class<?> clazz = beanDefinitionMap.get(name);
            if (clazz != null) {
                // 如果找到 Bean 的定义，则创建 Bean 的实例
                try {
                    bean = createBean(name, clazz);
                    beanMap.put(name, bean);
                } catch (Exception e) {
                    // 如果创建 Bean 的实例失败，则抛出异常
                    throw new RuntimeException(e);
                }
            } else {
                // 如果没有找到 Bean 的定义，则抛出异常
                throw new IllegalArgumentException("No bean named " + name + " is registered");
            }
        }
        return bean;
    }

    /**
     * 创建 Bean 的实例，并进行构造器注入
     *
     * @param name  Bean 的名称
     * @param clazz Bean 的类
     * @return Bean 的实例
     * @throws Exception 如果创建 Bean 的实例失败
     */
    public Object createBean(String name, Class<?> clazz) throws Exception {
        // 检测循环依赖：如果当前 Bean 正在创建，则抛出异常
        if (creatingBeans.contains(name)) {
            throw new RuntimeException("Circular dependency detected: " + name);
        }
        // 将当前 Bean 的名称添加到正在创建的集合中
        creatingBeans.add(name);

        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            Object bean;
            if (constructors.length == 0) {
                // 如果没有构造函数，则使用默认构造函数
                bean = clazz.newInstance();
            } else {
                // 获取 Bean 的第一个构造函数
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
                    dependencies[i] = getBean(dependencyName);
                }
                // 使用构造函数和依赖的 Bean 的实例创建 Bean 的实例
                bean = constructor.newInstance(dependencies);
            }
            // Setter 注入
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                    Class<?> parameterType = method.getParameterTypes()[0];
                    String dependencyName = Utils.lowerCaseFirstLetter(parameterType.getSimpleName());
                    Object dependency = getBean(dependencyName);
                    method.invoke(bean, dependency);
                }
            }
            return bean;
        } finally {
            // 从正在创建的集合中移除当前 Bean 的名称
            creatingBeans.remove(name);
        }
    }
}