package core.cfgRd;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.enums.ScopeType;
import core.ioc.BeanDefinition;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class that reads bean definitions from a JSON file using Gson for simplified object mapping.
 * It implements the BeanDefinitionReader interface, enabling the IoC container to load bean definitions from JSON configurations.
 */
public class JsonBeanDefinitionReader implements BeanDefinitionReader {

    /**
     * Loads bean definitions from the specified JSON file, parses them, and returns a map of BeanDefinition objects.
     *
     * @param location The location of the JSON file (resource path relative to the classpath).
     * @return A map of bean definitions, where the key is the bean ID and the value is the corresponding BeanDefinition object.
     * Returns an empty map if an error occurs during parsing.
     */
    @Override
    public Map<String, BeanDefinition> loadBeanDefinitions(String location) {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
        try {
            // Load the JSON file from the classpath as an InputStream, ensuring resource availability.
            InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(location)));

            // Parse the JSON content into a JsonObject, which represents the root of the JSON structure.
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            System.out.println("jsonObject: " + jsonObject);
            System.out.println("jsonObject type: " + jsonObject.getClass().getName());

            // Retrieve the "beans" array from the root JsonObject, which contains the list of bean definitions.
            JsonArray beansArray = jsonObject.getAsJsonArray("beans");

            System.out.println("beansArray: " + beansArray);
            System.out.println("beansArray type: " + beansArray.getClass().getName());

            // Initialize Gson, a library used for converting JSON to Java objects, to facilitate property mapping.
            Gson gson = new Gson();

            // Iterate over the beans array to process each bean definition.
            for (int i = 0; i < beansArray.size(); i++) {
                // Get the current bean definition as a JsonObject.
                JsonObject beanObject = beansArray.get(i).getAsJsonObject();

                // Extract the bean ID and class name from the current bean definition.
                String id = beanObject.get("id").getAsString();
                String className = beanObject.get("class").getAsString();

                // Log the processing of the current bean for debugging purposes.
                System.out.println("Processing bean: " + id + ", class: " + className);

                // Obtain the context class loader to load the bean class.
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

                try {
                    // Load the bean class using the class loader.
                    Class<?> clazz = classLoader.loadClass(className);

                    // Log the successful loading of the bean class.
                    System.out.println("Class loaded successfully: " + className);

                    // Create a new BeanDefinition object to store the bean's metadata.
                    BeanDefinition beanDefinition = new BeanDefinition();

                    // Set the bean's ID and class in the BeanDefinition object.
                    beanDefinition.setId(id);
                    beanDefinition.setBeanClass(clazz);

                    // Handle the bean's scope (singleton or prototype), defaulting to singleton if not specified.
                    String scope = beanObject.has("scope") ? beanObject.get("scope").getAsString() : "SINGLETON";
                    beanDefinition.setScope(ScopeType.valueOf(scope.toUpperCase()));

                    // Handle property injection by extracting property definitions from the "properties" JsonObject.
                    JsonObject propertiesJson = beanObject.getAsJsonObject("properties");
                    if (propertiesJson != null) {
                        // Use Gson to convert the "properties" JsonObject to a Property object for easy access.
                        Property property = gson.fromJson(propertiesJson, Property.class);

                        // Create a map to store property values, specifically for "dataSource" reference.
                        Map<String, String> propertyValues = new HashMap<>();
                        propertyValues.put("dataSource", property.getRef());

                        // Set the property values in the BeanDefinition object.
                        beanDefinition.setPropertyValues(propertyValues);
                    }

                    // Register the completed BeanDefinition object in the beanDefinitions map.
                    beanDefinitions.put(id, beanDefinition);

                    // Log the successful registration of the bean definition.
                    System.out.println("Bean definition registered: " + id);

                } catch (ClassNotFoundException e) {
                    // Handle class loading failures by logging the error and printing the stack trace.
                    System.err.println("Failed to load class: " + className);
                    System.err.println("ClassNotFoundException: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // Handle any other exceptions that might occur during JSON parsing or file reading.
            e.printStackTrace();
        }

        // Return the map containing all loaded bean definitions.
        return beanDefinitions;
    }
}