package src.com.es2.designpatterns;

import java.util.List;
import java.util.Map;

import src.com.es2.designpatterns.Configuration.ConfigurationManager;
import src.com.es2.designpatterns.Credential.*;
import src.com.es2.designpatterns.ResourcePool.ResourcePoolManager;
import src.com.es2.designpatterns.Storage.StorageFactory;
import src.com.es2.designpatterns.Storage.StorageType;
import src.com.es2.designpatterns.Storage.Implementors.FileStorageImplementor;
import src.com.es2.designpatterns.StructuredManagement.CategoryManager;
import src.com.es2.designpatterns.StructuredManagement.PasswordCategory;
import src.com.es2.designpatterns.StructuredManagement.PasswordEntry;
import src.com.es2.designpatterns.StructuredManagement.PasswordItem;
import src.com.es2.designpatterns.ResourcePool.EncryptionEngine;
import src.com.es2.designpatterns.ResourcePool.ResourcePoolManager;
import src.com.es2.designpatterns.ResourcePool.SecureConnection;

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






        // Get the category manager singleton
        CategoryManager manager = CategoryManager.getInstance();
        System.out.println("===== Testing Password Category Management =====\n");
        Credential trelloCredential = factory.createCredential(CredentialType.PASSWORD);
        Credential phoneCredential = factory.createCredential(CredentialType.PASSWORD);
        // Create the hierarchy from the example:
        // Pessoal->Produtividade->Trello->*(Password)*
        // Pessoal->Telefone->*(Password)*
        System.out.println("Creating category hierarchy...");
        
        // Method 1: Create categories step by step
        PasswordCategory pessoal = manager.createRootCategory("Pessoal");
        PasswordCategory produtividade = manager.createSubcategory(pessoal.getId(), "Produtividade");
        PasswordCategory trello = manager.createSubcategory(produtividade.getId(), "Trello");
        
        // Add a password to Trello category
        PasswordEntry trelloEntry = manager.createPasswordEntry(
            trello.getId(), "Conta Trello", trelloCredential);
        
        // Method 2: Create categories using a path
        PasswordCategory telefone = manager.createCategoryPath("Pessoal->Telefone");
        
        // Add a password to Telefone category
        PasswordEntry phoneEntry = manager.createPasswordEntry(
            telefone.getId(), "PIN Telefone", phoneCredential);
        
        // Display the entire structure
        System.out.println("\nCategory Structure:");
        manager.displayAll();
        
        // Demonstrate getting the full path of an item
        System.out.println("\nFull paths of password entries:");
        System.out.println("- " + trelloEntry.getPath());
        System.out.println("- " + phoneEntry.getPath());
        
        // Demonstrate counting passwords
        System.out.println("\nCounting passwords:");
        System.out.println("Passwords in 'Pessoal': " + pessoal.countPasswords());
        System.out.println("Passwords in 'Produtividade': " + produtividade.countPasswords());
        System.out.println("Passwords in 'Trello': " + trello.countPasswords());
        System.out.println("Passwords in 'Telefone': " + telefone.countPasswords());
        
        // Demonstrate searching
        System.out.println("\nSearching for 'tel':");
        List<PasswordItem> searchResults = manager.search("tel");
        for (PasswordItem item : searchResults) {
            System.out.println("- " + item.getName() + " (" + item.getPath() + ")");
        }
        
        // Demonstrate removing items
        System.out.println("\nRemoving 'Trello' category...");
        manager.removeItem(trello.getId());
        manager.displayAll();
        
        // Create another password entry directly in 'Pessoal'
        System.out.println("\nAdding a password directly to 'Pessoal'...");
        Credential emailCredential = factory.createCredential(CredentialType.PASSWORD);
        @SuppressWarnings("unused")
        PasswordEntry emailEntry = manager.createPasswordEntry(
            pessoal.getId(), "Email Pessoal", emailCredential);
        manager.displayAll();



        System.out.println("===== Testing Resource Pool Pattern =====\n");
        // Get the resource pool manager
        ResourcePoolManager resmanager = ResourcePoolManager.getInstance();
        
        try { 
            // Test secure connections
            ResourcePoolTest.testSecureConnections(resmanager);
            
            // Test encryption engines
            ResourcePoolTest.testEncryptionEngines(resmanager);
            
            // Test resource reuse
            ResourcePoolTest.testResourceReuse(resmanager);
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            // Make sure to close all resources
            resmanager.closeAll();
        }
    
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