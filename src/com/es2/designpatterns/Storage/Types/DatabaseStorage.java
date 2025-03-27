package src.com.es2.designpatterns.Storage.Types;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Storage.Storage;
import src.com.es2.designpatterns.Storage.StorageType;

import java.util.HashMap;
import java.util.Map;

public class DatabaseStorage implements Storage, StorageAllocation {
    private Map<String, Credential> dbStorage = new HashMap<>();

    @Override
    public void saveCredential(Credential credential) {
        dbStorage.put(credential.getId(), credential);
        System.out.println("Credential saved in Database Storage.");
    }

    @Override
    public Credential retrieveCredential(String id) {
        return dbStorage.get(id);
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.DATABASE;
    }

    @Override
    public void allocateStorage() {
        System.out.println("Allocating database storage resources.");
    }
}
