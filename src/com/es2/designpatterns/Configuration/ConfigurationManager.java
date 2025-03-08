package src.com.es2.designpatterns.Configuration;

import java.util.HashMap;
import java.util.Map;

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
    
    /* Getter for configuration
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
        
    }
    
    // Save current configurations
    public void saveConfigurations() {
        
    }
    
    private void loadDefaultConfigurations() {
        configurations.put("maxPasswordLength", 32);
        configurations.put("defaultStorageType", "FILE");
        configurations.put("encryptionAlgorithm", "AES-256");
        configurations.put("maxPoolConnections", 10);
    }
}
