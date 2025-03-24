package src.com.es2.designpatterns.Credential;

public class FileStorageFactory implements StorageFactory {
    @Override
    public CredentialStorage createStorage() {
        return new FileStorage();
    }
}
