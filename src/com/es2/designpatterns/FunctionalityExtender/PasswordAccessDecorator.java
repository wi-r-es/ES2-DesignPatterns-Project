package src.com.es2.designpatterns.FunctionalityExtender;

import src.com.es2.designpatterns.Storage.StorageFactory;
import src.com.es2.designpatterns.Storage.StorageType;

public abstract class PasswordAccessDecorator implements PasswordAccessHandler {
    protected PasswordAccessHandler wrapped;

    public PasswordAccessDecorator(PasswordAccessHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void access(String credentialId, StorageFactory storageFactory) {
        wrapped.access(credentialId, storageFactory);
    }

    @Override
    public void access(String credentialId, StorageFactory storageFactory, StorageType type) {
        wrapped.access(credentialId, storageFactory, type);
    }
}
