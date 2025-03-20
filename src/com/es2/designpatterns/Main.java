package src.com.es2.designpatterns;

import java.util.Map;

import src.com.es2.designpatterns.Configuration.ConfigurationManager;
import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Credential.CredentialFactory;
import src.com.es2.designpatterns.Credential.CredentialType;
import src.com.es2.designpatterns.Credential.Generator.PasswordGenerator;
import src.com.es2.designpatterns.Credential.SecurityCriteria;

public class Main {
    
    public static void main(String[] args) {
        ConfigurationManager configManager = ConfigurationManager.getInstance();
        CredentialFactory instance = CredentialFactory.getInstance();
        configManager.loadConfigurations("config.properties");
        int maxLength = configManager.getConfiguration("maxPasswordLength");
        String storageType = configManager.getConfiguration("defaultStorageType");

        System.out.println("Configuration Manager Settings. MaxLength:" + maxLength + " StorageType: " + storageType  );

        configManager.setConfiguration("defaultStorageType", "CLOUD");
        configManager.setConfiguration("maxPasswordLength", 64);
        configManager.setConfiguration("newSetting", "This is a new setting");
        configManager.setConfiguration("debugMode", true);

        // System.out.println("Configuration Manager Settings. MaxLength:" + configManager.getConfiguration("maxPasswordLength") + " StorageType: " + configManager.getConfiguration("defaultStorageType")  );
        displayCurrentConfigurations(configManager);

        System.out.println("\nSaving Configurations");
        configManager.saveConfigurations();
        System.out.println("\nLoading Configurations");
        configManager.loadConfigurations("config.properties");

        Credential passwordStandard = instance.createCredential(CredentialType.PASSWORD, new SecurityCriteria.Builder().build()) ;
        Credential passwordEnhanced = instance.createCredential(CredentialType.PASSWORD, new SecurityCriteria.Builder().algorithm("enhanced").build()) ;
        Credential API = instance.createCredential(CredentialType.API_KEY, null);
        Credential SecretKey = instance.createCredential(CredentialType.SECRET_KEY, null);
        System.out.println("Generated password: " + passwordStandard);
        System.out.println("Generated password: " + passwordEnhanced);
        System.out.println("Generated API: " + API);
        System.out.println("Generated SecretKey: " + SecretKey);
    }



    private static void displayCurrentConfigurations(ConfigurationManager configManager) {
        System.out.println("Current configurations:");
        
        Map<String, Object> configs = configManager.getConfigurations();
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue() + 
                            " (Type: " + (entry.getValue() != null ? entry.getValue().getClass().getSimpleName() : "null") + ")");
        }
    }   

}