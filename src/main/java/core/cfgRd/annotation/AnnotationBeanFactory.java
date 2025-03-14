package core.cfgRd.annotation;

import core.ioc.DefaultListableBeanFactory;

import java.util.Map;

/**
 * Bean factory that reads bean definitions from annotated classes.
 */
public class AnnotationBeanFactory {

    private DefaultListableBeanFactory beanFactory;
    private AnnotationBeanDefinitionReader reader;

    /**
     * Constructs an AnnotationBeanFactory with the specified bean factory.
     * Scans the entire classpath for annotated classes.
     *
     * @param beanFactory The DefaultListableBeanFactory to register bean definitions with.
     */
    public AnnotationBeanFactory(DefaultListableBeanFactory beanFactory) {
        this(null, beanFactory);
    }

    /**
     * Constructs an AnnotationBeanFactory with the specified base package and bean factory.
     * If basePackage is null or empty, scans the entire classpath.
     *
     * @param basePackage The base package to scan for annotated classes, or null/empty to scan the entire classpath.
     * @param beanFactory The DefaultListableBeanFactory to register bean definitions with.
     */
    public AnnotationBeanFactory(String basePackage, DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.reader = new AnnotationBeanDefinitionReader();
        Map<String, core.ioc.BeanDefinition> beanDefinitions = reader.loadBeanDefinitions(basePackage);
        beanDefinitions.forEach(beanFactory::registerBeanDefinition);
    }

    /**
     * Get the bean with the specified ID.
     *
     * @param id The ID of the bean to retrieve.
     * @return The bean instance, or null if no bean with the specified ID is found.
     */
    public Object getBean(String id) {
        return beanFactory.getBean(id);
    }
}