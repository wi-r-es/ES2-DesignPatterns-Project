package src.com.es2.designpatterns.Storage.Implementors;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Storage.StorageImplementor;
import java.util.HashMap;
import java.util.Map;

public class FileStorageImplementor implements StorageImplementor {
    private Map<String, Credential> fileStorage = new HashMap<>();

    @Override
    public void storeCredential(Credential credential) {
        fileStorage.put(credential.getId(), credential);
        System.out.println("Credential saved in File Storage implementation.");
    }

    @Override
    public Credential retrieveCredential(String id) {
        return fileStorage.get(id);
    }

    @Override
    public void allocateStorage() {
        System.out.println("Allocating file storage resources.");
    }
}
