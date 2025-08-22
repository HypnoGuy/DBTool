package com.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * A generic class to enhance a POJO by providing access to its properties via a Map-like interface.
 * This class uses Java Reflection to dynamically get and set properties based on their names.
 */
public class PropertyMapper {

    private final Object pojo;
    private final Map<String, Method> getterMethods;
    private final Map<String, Method> setterMethods;

    /**
     * Constructs a PojoEnhancer for a given POJO.
     * It scans the POJO's class for public getter and setter methods
     * and maps them for later use.
     *
     * @param pojo The POJO instance to be enhanced.
     * @throws IllegalArgumentException if the provided object is null.
     */
    public PropertyMapper(Object pojo) {
        if (pojo == null) {
            throw new IllegalArgumentException("POJO cannot be null.");
        }
        this.pojo = pojo;

        // Get the class of the POJO to access its methods
        Class<?> pojoClass = pojo.getClass();

        // Use Java Streams to filter and collect getter and setter methods
        this.getterMethods = Arrays.stream(pojoClass.getMethods())
                .filter(method -> method.getName().startsWith("get") && method.getParameterCount() == 0 && method.getReturnType() != void.class)
                .collect(Collectors.toMap(
                        method -> toPropertyName(method.getName()),
                        method -> method
                ));

        this.setterMethods = Arrays.stream(pojoClass.getMethods())
                .filter(method -> method.getName().startsWith("set") && method.getParameterCount() == 1)
                .collect(Collectors.toMap(
                        method -> toPropertyName(method.getName()),
                        method -> method
                ));
    }

    /**
     * Converts a method name (e.g., "getFirstName") to a property name (e.g., "firstName").
     *
     * @param methodName The name of the getter or setter method.
     * @return The corresponding property name.
     */
    private String toPropertyName(String methodName) {
        // Remove "get" or "set" prefix and convert the first letter to lowercase
        String propertyName = methodName.substring(3);
        return Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
    }

    /**
     * Gets a Map of all properties and their current values from the POJO.
     *
     * @return A Map where keys are property names and values are their corresponding values.
     */
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();
        for (String propertyName : getterMethods.keySet()) {
            try {
                properties.put(propertyName, getProperty(propertyName));
            } catch (Exception e) {
                // You could log this error or handle it as needed
                System.err.println("Error getting property '" + propertyName + "': " + e.getMessage());
            }
        }
        return properties;
    }

    /**
     * Gets the value of a specific property by its name.
     *
     * @param propertyName The name of the property to retrieve.
     * @return The value of the property.
     * @throws NoSuchMethodException If a getter for the property does not exist.
     * @throws InvocationTargetException If an exception occurs during method invocation.
     * @throws IllegalAccessException If the getter method is not accessible.
     */
    public Object getProperty(String propertyName)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = getterMethods.get(propertyName);
        if (getter == null) {
            throw new NoSuchMethodException("No getter found for property: " + propertyName);
        }
        return getter.invoke(pojo);
    }

    /**
     * Sets the value of a specific property by its name.
     *
     * @param propertyName The name of the property to set.
     * @param value The new value for the property.
     * @throws NoSuchMethodException If a setter for the property does not exist.
     * @throws InvocationTargetException If an exception occurs during method invocation.
     * @throws IllegalAccessException If the setter method is not accessible.
     * @throws IllegalArgumentException If the value's type does not match the setter's parameter type.
     */
    public void setProperty(String propertyName, Object value)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Method setter = setterMethods.get(propertyName);
        if (setter == null) {
            throw new NoSuchMethodException("No setter found for property: " + propertyName);
        }
        setter.invoke(pojo, value);
    }

    // --- Example POJO Class ---
    public static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }
}
