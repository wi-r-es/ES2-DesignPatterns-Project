package src.com.es2.designpatterns.FunctionalityExtender;

import src.com.es2.designpatterns.Storage.StorageFactory;
import src.com.es2.designpatterns.Storage.StorageType;

public class BasicPasswordAccessHandler implements PasswordAccessHandler {
    @Override
    public void access(String credentialId, StorageFactory storageFactory) {
        storageFactory.printCredential(credentialId);
    }

    @Override
    public void access(String credentialId, StorageFactory storageFactory, StorageType type) {
        storageFactory.printCredential(type, credentialId);
    }
}
