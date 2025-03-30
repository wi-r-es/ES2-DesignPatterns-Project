// StorageFactory.java - Factory that creates bridges and manages default storage
package src.com.es2.designpatterns.Storage;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Storage.Implementors.CloudStorageImplementor;
import src.com.es2.designpatterns.Storage.Implementors.DatabaseStorageImplementor;
import src.com.es2.designpatterns.Storage.Implementors.FileStorageImplementor;

public class StorageFactory {
    // Singleton pattern
    private static StorageFactory instance;
    
    // Storage implementors
    private final StorageImplementor cloudImplementor;
    private final StorageImplementor databaseImplementor;
    private final StorageImplementor fileImplementor;
    
    // Storage instances (using the Bridge pattern)
    private final Storage cloudStorage;
    private final Storage databaseStorage;
    private final Storage fileStorage;
    
    // Default storage to use
    private Storage defaultStorage;
    
    private StorageFactory(StorageType defaultType) {
        // Create implementors
        this.cloudImplementor = new CloudStorageImplementor();
        this.databaseImplementor = new DatabaseStorageImplementor();
        this.fileImplementor = new FileStorageImplementor();
        
        // Initialize storage with implementors (Bridge pattern)
        this.cloudStorage = new CredentialStorage(cloudImplementor, StorageType.CLOUD);
        this.databaseStorage = new CredentialStorage(databaseImplementor, StorageType.DATABASE);
        this.fileStorage = new CredentialStorage(fileImplementor, StorageType.FILE);
        
        // Set the default storage based on the specified type
        setDefaultStorage(defaultType);
        
        // Allocate storage resources
        cloudImplementor.allocateStorage();
        databaseImplementor.allocateStorage();
        fileImplementor.allocateStorage();
    }
    
    public static synchronized StorageFactory getInstance(StorageType defaultType) {
        if (instance == null) {
            instance = new StorageFactory(defaultType);
        }
        return instance;
    }
    
    // Method to change the default storage type
    public void setDefaultStorage(StorageType type) {
        switch (type) {
            case CLOUD:
                defaultStorage = cloudStorage;
                break;
            case DATABASE:
                defaultStorage = databaseStorage;
                break;
            case FILE:
                defaultStorage = fileStorage;
                break;
        }
        System.out.println("Default storage set to " + type);
    }
    
    // Method to save a credential using the default storage
    public void saveCredential(Credential credential) {
        defaultStorage.saveCredential(credential);
    }
    
    // Method to retrieve a credential from a specific storage type
    public Credential retrieveCredential(StorageType type, String id) {
        Storage storage = getStorageByType(type);
        return storage.retrieveCredential(id);
    }
    
    // Method to retrieve a credential from the default storage
    public Credential retrieveCredential(String id) {
        return defaultStorage.retrieveCredential(id);
    }
    
    // Method to print credential information
    public void printCredential(StorageType type, String id) {
        Credential credential = retrieveCredential(type, id);
        printCredentialInfo(credential, type);
    }
    
    // Method to print credential information from default storage
    public void printCredential(String id) {
        Credential credential = retrieveCredential(id);
        printCredentialInfo(credential, defaultStorage.getStorageType());
    }
    
    // Helper method to print credential information
    private void printCredentialInfo(Credential credential, StorageType type) {
        if (credential != null) {
            System.out.println("Credential ID: " + credential.getId() +
                    " | Credential: " + credential.getName() +
                    " | Stored in: " + type);
        } else {
            System.out.println("Credential was null");
        }
    }
    
    // Helper method to get the appropriate storage instance
    private Storage getStorageByType(StorageType type) {
        switch (type) {
            case CLOUD:
                return cloudStorage;
            case DATABASE:
                return databaseStorage;
            case FILE:
                return fileStorage;
            default:
                throw new IllegalArgumentException("Storage type not recognized: " + type);
        }
    }
    
    // Method to demonstrate the flexibility of the Bridge pattern
    // This allows changing implementors at runtime
    public void switchImplementor(StorageType storageType, StorageImplementor newImplementor) {
        Storage storage = getStorageByType(storageType);
        storage.setImplementor(newImplementor);
        System.out.println("Switched implementor for " + storageType);
    }
}