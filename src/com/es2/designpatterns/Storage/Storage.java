package src.com.es2.designpatterns.Storage;

import src.com.es2.designpatterns.Credential.Credential;

public interface Storage {
    void saveCredential(Credential credential);
    Credential retrieveCredential(String id);
    StorageType getStorageType();
}
