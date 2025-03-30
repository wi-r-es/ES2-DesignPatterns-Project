package src.com.es2.designpatterns;

import java.util.Map;

import src.com.es2.designpatterns.Configuration.ConfigurationManager;
import src.com.es2.designpatterns.Credential.*;
import src.com.es2.designpatterns.Credential.Generator.PasswordGenerator;
import src.com.es2.designpatterns.Storage.StorageFactory;
import src.com.es2.designpatterns.Storage.StorageType;

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

        configManager.setConfiguration("defaultStorageType", "CLOUD");
        configManager.setConfiguration("maxPasswordLength", 64);
        configManager.setConfiguration("newSetting", "This is a new setting");
        configManager.setConfiguration("debugMode", true);

        // Criar factory storage
        StorageFactory storageFactory = new StorageFactory(configManager.getConfiguration("defaultStorageType"));

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

        storageFactory.createStorage(passwordEnhanced, null);
        storageFactory.createStorage(passwordStandard, StorageType.FILE.toString());
        storageFactory.createStorage(passwordStandard, StorageType.DATABASE.toString());
        storageFactory.createStorage(secretKey, StorageType.CLOUD.toString());

        String StandardpasswordId = passwordStandard.getId();
        String EnhancedpasswordId = passwordEnhanced.getId();
        String apiKeyId = apiKey.getId();
        String secretKeyId = secretKey.getId();

        // Recuperar e imprimir as credenciais armazenadas
        System.out.println("\nCredenciais Armazenadas em CLOUD:");
        // Cloud
        storageFactory.printCredential(StorageType.CLOUD, EnhancedpasswordId);
        storageFactory.printCredential(StorageType.CLOUD, StandardpasswordId);
        storageFactory.printCredential(StorageType.CLOUD, apiKeyId);
        storageFactory.printCredential(StorageType.CLOUD, secretKeyId);
        // File
        System.out.println("\nCredenciais Armazenadas em FILE:");
        storageFactory.printCredential(StorageType.FILE, EnhancedpasswordId);
        storageFactory.printCredential(StorageType.FILE, StandardpasswordId);
        storageFactory.printCredential(StorageType.FILE, apiKeyId);
        storageFactory.printCredential(StorageType.FILE, secretKeyId);
        // DataBase
        System.out.println("\nCredenciais Armazenadas em DATABASE:");
        storageFactory.printCredential(StorageType.DATABASE, EnhancedpasswordId);
        storageFactory.printCredential(StorageType.DATABASE, StandardpasswordId);
        storageFactory.printCredential(StorageType.DATABASE, apiKeyId);
        storageFactory.printCredential(StorageType.DATABASE, secretKeyId);
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