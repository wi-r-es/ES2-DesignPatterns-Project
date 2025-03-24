package src.com.es2.designpatterns.Configuration;

import src.com.es2.designpatterns.Credential.CredentialType;
import src.com.es2.designpatterns.Credential.SecurityCriteria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//Singleton Pattern
public class ConfigurationManager {
    // The single instance of the class
    private static ConfigurationManager instance;

    // Configuration storage
    private Map<String, Object> configurations;

    // Private constructor to prevent instantiation from outside
    private ConfigurationManager() {
        configurations = new HashMap<>();
        loadDefaultConfigurations();
    }

    // Singleton pattern
    public static synchronized ConfigurationManager getInstance() {
        // Lazy initialization
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("Singleton instance: " + this);
    }

    /* Getter for configuration -- https://www.geeksforgeeks.org/generics-in-java/
        Using T -> Generic types for ease of use with whichever class we want to use
    */
    @SuppressWarnings("unchecked")
    public <T> T getConfiguration(String key) {
        return (T) configurations.get(key);
    }

    /*  Setter for configuration

     */
    public <T> void setConfiguration(String key, T value) {
        configurations.put(key, value);
    }

    // Load configurations from a source (file, database, etc.)
    public void loadConfigurations(String source) {
        File configFile = new File(source);

        if (!configFile.exists()) {
            Logger.getLogger(ConfigurationManager.class.getName())
                    .log(Level.WARNING, "Configuration file {0} not found. Using default values.", source);
            return;
        }

        try (FileInputStream fis = new FileInputStream(configFile)) {
            Properties props = new Properties();
            props.load(fis);

            // Clear current configurations
            configurations.clear();

            // Load properties into configurations map with appropriate type conversion
            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);

                // Handle different types of configurations (basic type conversion)
                if (key.endsWith(".int")) {
                    String actualKey = key.substring(0, key.length() - 4);
                    configurations.put(actualKey, Integer.parseInt(value));
                } else if (key.endsWith(".boolean")) {
                    String actualKey = key.substring(0, key.length() - 8);
                    configurations.put(actualKey, Boolean.parseBoolean(value));
                } else if (key.endsWith(".double")) {
                    String actualKey = key.substring(0, key.length() - 7);
                    configurations.put(actualKey, Double.parseDouble(value));
                } else {
                    configurations.put(key, value);
                }
            }

            Logger.getLogger(ConfigurationManager.class.getName())
                    .log(Level.INFO, "Successfully loaded configurations from {0}", source);

        } catch (IOException e) {
            Logger.getLogger(ConfigurationManager.class.getName())
                    .log(Level.SEVERE, "Error loading configurations from " + source, e);
        }
    }

    // Save current configurations
    public void saveConfigurations() {
        // Get the configured save location or use default
        String configFilePath = (String) configurations.get("configFilePath");
        if (configFilePath == null) {
            configFilePath = "config.properties";
        }

        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            Properties props = new Properties(); //https://www.geeksforgeeks.org/java-util-properties-class-java/

            // Convert configurations to properties with type hints
            for (Map.Entry<String, Object> entry : configurations.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Integer) {
                    props.setProperty(key + ".int", value.toString());
                } else if (value instanceof Boolean) {
                    props.setProperty(key + ".boolean", value.toString());
                } else if (value instanceof Double) {
                    props.setProperty(key + ".double", value.toString());
                } else {
                    props.setProperty(key, value != null ? value.toString() : "");
                }
            }

            // Save properties to file
            props.store(fos, "Password Manager Configuration");

            Logger.getLogger(ConfigurationManager.class.getName())
                    .log(Level.INFO, "Successfully saved configurations to {0}", configFilePath);

        } catch (IOException e) {
            Logger.getLogger(ConfigurationManager.class.getName())
                    .log(Level.SEVERE, "Error saving configurations to " + configFilePath, e);
        }
    }

    private void loadDefaultConfigurations() {
        configurations.put("maxPasswordLength", 32);
        configurations.put("defaultStorageType", "FILE");
        configurations.put("encryptionAlgorithm", "AES-256");
        configurations.put("maxPoolConnections", 10);
    }

    // Getter return a copy to prevent direct modification
    public Map<String, Object> getConfigurations() {
        return new HashMap<>(configurations);
    }
}


