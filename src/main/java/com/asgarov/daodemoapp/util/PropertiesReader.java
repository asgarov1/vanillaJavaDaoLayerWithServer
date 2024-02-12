package com.asgarov.daodemoapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final String DEFAULT_PROPERTIES_FILE = "application.properties";


    /**
     * Method to read specified properties file
     *
     * @param fileName is the file that will be looked for
     * @return Properties
     */
    public static Properties getProperties(String fileName) {
        try (InputStream input = PropertiesReader.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Convenience method to read default properties file if no other file name was specified
     *
     * @return Properties
     */
    public static Properties getProperties() {
        return getProperties(DEFAULT_PROPERTIES_FILE);
    }

    /**
     * Convenience method to read a property value by key
     * from the default properties file
     *
     * @return Properties
     */
    public static String getProperty(String propertyKey) {
        return getProperties().getProperty(propertyKey);
    }

    public static void main(String[] args) {
        System.out.println(PropertiesReader.getProperties());
        System.out.println(PropertiesReader.getProperties().getProperty("datasource.url"));
    }
}
