package src.com.es2.designpatterns.Credential;

public class DatabaseStorageFactory implements StorageFactory {
    @Override
    public CredentialStorage createStorage() {
        return new DatabaseStorage();
    }
}
