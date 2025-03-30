package src.com.es2.designpatterns.Storage.Implementors;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Storage.StorageImplementor;
import java.util.HashMap;
import java.util.Map;

public class DatabaseStorageImplementor implements StorageImplementor {
    private Map<String, Credential> dbStorage = new HashMap<>();
    
    @Override
    public void storeCredential(Credential credential) {
        dbStorage.put(credential.getId(), credential);
        System.out.println("Credential saved in Database Storage implementation.");
    }
    
    @Override
    public Credential retrieveCredential(String id) {
        return dbStorage.get(id);
    }
    
    @Override
    public void allocateStorage() {
        System.out.println("Allocating database storage resources.");
    }
}