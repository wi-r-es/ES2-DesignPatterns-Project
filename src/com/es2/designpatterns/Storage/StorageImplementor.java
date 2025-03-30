package src.com.es2.designpatterns.Storage;

import src.com.es2.designpatterns.Credential.Credential;

public interface StorageImplementor {
    void storeCredential(Credential credential);
    Credential retrieveCredential(String id);
    void allocateStorage();
}