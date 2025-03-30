package src.com.es2.designpatterns;

import java.util.Map;

import src.com.es2.designpatterns.Configuration.ConfigurationManager;
import src.com.es2.designpatterns.Credential.*;
import src.com.es2.designpatterns.Storage.StorageFactory;
import src.com.es2.designpatterns.Storage.StorageType;
import src.com.es2.designpatterns.Storage.Implementors.FileStorageImplementor;

public class Main {

    public static void main(String[] args) {
        CredentialFactory factory = CredentialFactory.getInstance();

        ConfigurationManager configManager = ConfigurationManager.getInstance();
        configManager.loadConfigurations("config.properties");

        System.out.println("Configuration Manager Settings:");
        displayCurrentConfigurations(configManager);

        int maxLength = configManager.getConfiguration("maxPasswordLength");
        String storageType = configManager.getConfiguration("defaultStorageType");

        System.out.println("Configuration Manager Settings. MaxLength:" + maxLength + " StorageType: " + storageType  );

        configManager.setConfiguration("defaultStorageType", StorageType.CLOUD);
        configManager.setConfiguration("maxPasswordLength", 64);
        configManager.setConfiguration("newSetting", "This is a new setting");
        configManager.setConfiguration("debugMode", true);

        // Criar factory storage -  with a default storage type
        StorageFactory storageFactory = StorageFactory.getInstance(configManager.getConfiguration("defaultStorageType"));

        System.out.println("\nSaving Configurations");
        configManager.saveConfigurations();
        System.out.println("\nLoading Configurations");
        configManager.loadConfigurations("config.properties");

        Credential passwordStandard = factory.createCredential(CredentialType.PASSWORD);
        Credential passwordEnhanced = factory.createCredential(CredentialType.SECRET_KEY);
        Credential pin = factory.createCredential(CredentialType.PIN);
        Credential apiKey = factory.createCredential(CredentialType.API_KEY);
        Credential secretKey = factory.createCredential(CredentialType.SECRET_KEY);

        System.out.println("\nGenerated Credentials:");
        System.out.println(passwordStandard);
        System.out.println(passwordEnhanced);
        System.out.println(pin);
        System.out.println(apiKey);
        System.out.println(secretKey);

        String StandardpasswordId = passwordStandard.getId();
        String EnhancedpasswordId = passwordEnhanced.getId();
        String apiKeyId = apiKey.getId();
        String secretKeyId = secretKey.getId();

        System.out.println("\n\n\n-----------------------------------");
        storageFactory.saveCredential(passwordEnhanced);
        storageFactory.saveCredential(passwordStandard);
        System.out.println("\n########\n");
        //prints
        storageFactory.printCredential(StandardpasswordId);
        storageFactory.printCredential(EnhancedpasswordId);
        System.out.println("\n\n\n-----------------------------------");
        
        storageFactory.setDefaultStorage(StorageType.DATABASE);
        storageFactory.saveCredential(passwordStandard);
        storageFactory.saveCredential(secretKey);
        System.out.println("\n########\n");
        storageFactory.printCredential(StandardpasswordId);
        storageFactory.printCredential(secretKeyId);

        

        // Recuperar e imprimir as credenciais armazenadas
        // System.out.println("\nCredenciais Armazenadas em CLOUD:");
        
        // Cloud
        
        // storageFactory.printCredential(StorageType.CLOUD, StandardpasswordId);
        // storageFactory.printCredential(StorageType.CLOUD, apiKeyId);
        // storageFactory.printCredential(StorageType.CLOUD, secretKeyId);
        // File
        // System.out.println("\nCredenciais Armazenadas em FILE:");
        // storageFactory.printCredential(StorageType.FILE, EnhancedpasswordId);
        // storageFactory.printCredential(StorageType.FILE, StandardpasswordId);
        // storageFactory.printCredential(StorageType.FILE, apiKeyId);
        // storageFactory.printCredential(StorageType.FILE, secretKeyId);
        // DataBase
        // System.out.println("\nCredenciais Armazenadas em DATABASE:");
        // storageFactory.printCredential(StorageType.DATABASE, EnhancedpasswordId);
        // storageFactory.printCredential(StorageType.DATABASE, StandardpasswordId);
        // storageFactory.printCredential(StorageType.DATABASE, apiKeyId);
        // storageFactory.printCredential(StorageType.DATABASE, secretKeyId);

        System.out.println("\nCredencial Armazenada em FILE:");
        storageFactory.printCredential(StorageType.FILE, StandardpasswordId);
        System.out.println("\nCredencial Armazenada em DATABASE:");
        storageFactory.printCredential(StorageType.DATABASE, EnhancedpasswordId);

        // Create a custom implementor
        FileStorageImplementor customFileImpl = new FileStorageImplementor();
        
        // Switch the implementor for DATABASE storage
        storageFactory.switchImplementor(StorageType.FILE, customFileImpl);
        
        // Save another credential to the default storage (still DATABASE, but now with cloud implementor)
        // Credential password3 = new Credential("3", "SocialMediaPassword", "Social456!");
        System.out.println("\n\n\n-----------------------------------");
        storageFactory.saveCredential(secretKey);
        storageFactory.saveCredential(apiKey);
        
        // Print the credential - it should show it was stored in DATABASE
        // but internally it used the file implementor
        System.out.println("\nCredenciais... :");
        storageFactory.printCredential(secretKeyId);
        storageFactory.printCredential(apiKeyId);
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