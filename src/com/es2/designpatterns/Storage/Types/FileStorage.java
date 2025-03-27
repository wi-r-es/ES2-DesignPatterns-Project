package src.com.es2.designpatterns.Storage.Types;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Storage.Storage;
import src.com.es2.designpatterns.Storage.StorageType;
import src.com.es2.designpatterns.Storage.Types.StorageAllocation;

import java.util.HashMap;
import java.util.Map;

public class FileStorage implements Storage, StorageAllocation {
    private Map<String, Credential> fileStorage = new HashMap<>();

    @Override
    public void saveCredential(Credential credential) {
        fileStorage.put(credential.getId(), credential);
        System.out.println("Credential saved in File Storage.");
    }

    @Override
    public Credential retrieveCredential(String id) {
        return fileStorage.get(id);
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.FILE;
    }

    @Override
    public void allocateStorage() {
        System.out.println("Allocating file storage resources.");
    }
}
