package src.com.es2.designpatterns.Storage.Implementors;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Storage.StorageImplementor;
import java.util.HashMap;
import java.util.Map;

public class CloudStorageImplementor implements StorageImplementor {
    private Map<String, Credential> cloudStorage = new HashMap<>();
    
    @Override
    public void storeCredential(Credential credential) {
        cloudStorage.put(credential.getId(), credential);
        System.out.println("Credential saved in Cloud Storage implementation.");
    }
    
    @Override
    public Credential retrieveCredential(String id) {
        return cloudStorage.get(id);
    }
    
    @Override
    public void allocateStorage() {
        System.out.println("Allocating cloud storage resources.");
    }
}