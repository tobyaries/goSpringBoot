package core.cfgRd;

import core.ioc.BeanDefinition;

import java.util.Map;

/**
 * Interface for bean definition readers.
 * Implementations of this interface are responsible for reading bean definitions
 * from a specific source (e.g., JSON, XML, annotations) and converting them
 * into a map of BeanDefinition objects.
 */
public interface BeanDefinitionReader {

    /**
     * Load bean definitions from the specified location.
     *
     * @param location The location of the bean definitions (e.g., file path, resource path).
     * @return A map of bean definitions, where the key is the bean ID and the value is the BeanDefinition object.
     */
    Map<String, BeanDefinition> loadBeanDefinitions(String location);
}