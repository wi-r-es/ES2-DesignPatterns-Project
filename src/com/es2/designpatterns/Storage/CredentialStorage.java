package src.com.es2.designpatterns.Storage;

import src.com.es2.designpatterns.Credential.Credential;

public class CredentialStorage extends Storage {
    private StorageType storageType;
    
    public CredentialStorage(StorageImplementor implementor, StorageType storageType) {
        super(implementor);
        this.storageType = storageType;
    }
    
    @Override
    public void saveCredential(Credential credential) {
        // Pre-processing for credential if needed
        System.out.println("Preparing to store credential with ID: " + credential.getId());
        
        // Delegate to the implementor
        implementor.storeCredential(credential);
    }
    
    @Override
    public Credential retrieveCredential(String id) {
        // Delegate to the implementor
        Credential credential = implementor.retrieveCredential(id);
        
        // Post-processing for credential if needed
        if (credential != null) {
            System.out.println("Retrieved credential with ID: " + id);
        } else {
            System.out.println("Credential with ID: " + id + " not found");
        }
        
        return credential;
    }
    
    @Override
    public StorageType getStorageType() {
        return storageType;
    }
}