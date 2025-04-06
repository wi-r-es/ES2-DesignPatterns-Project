package src.com.es2.designpatterns.FunctionalityExtender;

import src.com.es2.designpatterns.Storage.StorageFactory;
import src.com.es2.designpatterns.Storage.StorageType;

public interface PasswordAccessHandler {
    void access(String credentialId, StorageFactory storageFactory);
    void access(String credentialId, StorageFactory storageFactory, StorageType type);
}