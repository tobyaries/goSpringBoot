package core.cfgRd.annotation;

import core.cfgRd.BeanDefinitionReader;
import core.enums.ScopeType;
import core.ioc.BeanDefinition;
import core.ioc.DefaultListableBeanFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads bean definitions from annotated classes across the classpath.
 * This class scans for classes annotated with @Component and registers them as bean definitions.
 */
public class AnnotationBeanDefinitionReader implements BeanDefinitionReader {

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    /**
     * Loads bean definitions from classes annotated with @Component.
     * If basePackage is null or empty, scans the entire classpath.
     *
     * @param basePackage The base package to scan for annotated classes, or null/empty to scan the entire classpath.
     * @return A map of bean definitions, where the key is the bean ID and the value is the BeanDefinition object.
     */
    @Override
    public Map<String, BeanDefinition> loadBeanDefinitions(String basePackage) {
        if (basePackage == null || basePackage.isEmpty()) {
            return loadAllBeanDefinitions(); // Scan the entire classpath
        } else {
            return loadPackageBeanDefinitions(basePackage); // Scan the specified package
        }
    }

    /**
     * Loads bean definitions from classes annotated with @Component within the specified base package.
     *
     * @param basePackage The base package to scan for annotated classes.
     * @return A map of bean definitions, where the key is the bean ID and the value is the BeanDefinition object.
     */
    private Map<String, BeanDefinition> loadPackageBeanDefinitions(String basePackage) {
        try {
            // Convert package name to resource path
            String resourcePath = basePackage.replace('.', '/');
            Enumeration<URL> resources = getClass().getClassLoader().getResources(resourcePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    scanClasses(directory, basePackage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }

    /**
     * Loads bean definitions from classes annotated with @Component across the entire classpath.
     *
     * @return A map of bean definitions, where the key is the bean ID and the value is the BeanDefinition object.
     */
    private Map<String, BeanDefinition> loadAllBeanDefinitions() {
        try {
            // Get all classes from the classpath
            Enumeration<URL> resources = getClass().getClassLoader().getResources("");
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                if ("file".equals(protocol)) {
                    String path = resource.getPath();
                    scanClasses(new File(path), "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }

    /**
     * Recursively scans the directory for classes annotated with @Component.
     *
     * @param directory   The directory to scan.
     * @param packageName The package name corresponding to the directory.
     */
    private void scanClasses(File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanClasses(file, packageName.isEmpty() ? file.getName() : packageName + "." + file.getName());
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName.isEmpty() ? file.getName().substring(0, file.getName().length() - 6) : packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    System.out.println("Scanning class: " + className);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(Component.class) && !Modifier.isAbstract(clazz.getModifiers())) {
                            Component component = clazz.getAnnotation(Component.class);
                            String beanId = component.value().isEmpty() ? clazz.getSimpleName() : component.value();

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setBeanClass(clazz);

                            if (clazz.isAnnotationPresent(Scope.class)) {
                                Scope scope = clazz.getAnnotation(Scope.class);
                                beanDefinition.setScope(ScopeType.valueOf(scope.value().toUpperCase()));
                            } else {
                                beanDefinition.setScope(ScopeType.SINGLETON);
                            }

                            beanDefinitions.put(beanId, beanDefinition);
                            System.out.println("Registered bean: " + beanId + " with class: " + className);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void injectDependencies(Object bean, Class<?> clazz, DefaultListableBeanFactory beanFactory) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                try {
                    System.out.println("Injecting dependency: " + field.getType().getName() + " into " + clazz.getName());
                    String beanName = findBeanNameByType(field.getType(), beanFactory);
                    System.out.println("Found bean name: " + beanName);
                    if (beanName != null) {
                        Object dependency = beanFactory.getBean(beanName);
                        field.set(bean, dependency);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String findBeanNameByType(Class<?> type, DefaultListableBeanFactory beanFactory) {
        for (Map.Entry<String, BeanDefinition> entry : beanFactory.getBeanDefinitionMap().entrySet()) {
            if (type.isAssignableFrom(entry.getValue().getBeanClass())) {
                return entry.getKey();
            }
        }
        return null;
    }
}