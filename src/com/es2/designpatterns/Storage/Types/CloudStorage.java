package src.com.es2.designpatterns.Storage.Types;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Storage.Storage;
import src.com.es2.designpatterns.Storage.StorageType;

import java.util.HashMap;
import java.util.Map;

public class CloudStorage implements Storage, StorageAllocation {
    private Map<String, Credential> cloudStorage = new HashMap<>();

    @Override
    public void saveCredential(Credential credential) {
        cloudStorage.put(credential.getId(), credential);
        System.out.println("Credential saved in Cloud Storage.");
    }

    @Override
    public Credential retrieveCredential(String id) {
        return cloudStorage.get(id);
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.CLOUD;
    }

    @Override
    public void allocateStorage() {
        System.out.println("Allocating cloud storage resources.");
    }
}
