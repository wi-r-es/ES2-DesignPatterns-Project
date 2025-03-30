package src.com.es2.designpatterns.Storage;

import src.com.es2.designpatterns.Credential.Credential;

public abstract class Storage {
    // Bridge to the implementor
    protected StorageImplementor implementor;
    
    public Storage(StorageImplementor implementor) {
        this.implementor = implementor;
    }
    
    // Common operations that all storage types share
    public abstract void saveCredential(Credential credential);
    public abstract Credential retrieveCredential(String id);
    public abstract StorageType getStorageType();
    
    // Method to change the implementor at runtime
    public void setImplementor(StorageImplementor implementor) {
        this.implementor = implementor;
    }
}