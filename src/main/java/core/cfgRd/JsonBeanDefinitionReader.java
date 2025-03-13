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

/**
 * A class that reads bean definitions from a JSON file using Gson.
 */
public class JsonBeanDefinitionReader implements BeanDefinitionReader {

    /**
     * Loads bean definitions from the specified JSON file.
     *
     * @param location The location of the JSON file (resource path).
     * @return A map of bean definitions, where the key is the bean ID and the value is the BeanDefinition object.
     */
    @Override
    public Map<String, BeanDefinition> loadBeanDefinitions(String location) {
        Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
        try {
            // Load the JSON file from the classpath
            InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(location));
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray beansArray = jsonObject.getAsJsonArray("beans");
            Gson gson = new Gson();

            // Iterate over the beans array and create BeanDefinition objects
            for (int i = 0; i < beansArray.size(); i++) {
                JsonObject beanObject = beansArray.get(i).getAsJsonObject();
                String id = beanObject.get("id").getAsString();
                String className = beanObject.get("class").getAsString();
                System.out.println("Processing bean: " + id + ", class: " + className);

                // Load the class using the context class loader
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    System.out.println("Class loaded successfully: " + className);
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setId(id);
                    beanDefinition.setBeanClass(clazz);

                    // Handle the scope of the bean
                    String scope = beanObject.has("scope") ? beanObject.get("scope").getAsString() : "SINGLETON";
                    beanDefinition.setScope(ScopeType.valueOf(scope.toUpperCase()));

                    // Handle property injection
                    JsonObject propertiesJson = beanObject.getAsJsonObject("properties");
                    if (propertiesJson != null) {
                        Property property = gson.fromJson(propertiesJson, Property.class);
                        Map<String, String> propertyValues = new HashMap<>();
                        propertyValues.put("dataSource", property.getRef());
                        beanDefinition.setPropertyValues(propertyValues);
                    }

                    beanDefinitions.put(id, beanDefinition);
                    System.out.println("Bean definition registered: " + id);
                } catch (ClassNotFoundException e) {
                    System.err.println("Failed to load class: " + className);
                    System.err.println("ClassNotFoundException: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }
}