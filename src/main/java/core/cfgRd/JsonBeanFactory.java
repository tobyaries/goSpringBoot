package core.cfgRd;

import core.ioc.DefaultListableBeanFactory;

import java.util.Map;

/**
 * Bean factory that reads bean definitions from a JSON file.
 */
public class JsonBeanFactory {

    private DefaultListableBeanFactory beanFactory;
    private BeanDefinitionReader reader;

    /**
     * Constructs a JsonBeanFactory with the specified configuration file and bean factory.
     *
     * @param configFilePath The path to the JSON configuration file (resource path).
     * @param beanFactory The DefaultListableBeanFactory to register bean definitions with.
     */
    public JsonBeanFactory(String configFilePath, DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.reader = new JsonBeanDefinitionReader();
        Map<String, core.ioc.BeanDefinition> beanDefinitions = reader.loadBeanDefinitions(configFilePath);
        beanDefinitions.forEach((id, definition) -> beanFactory.registerBeanDefinition(id, definition));
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