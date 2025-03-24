package src.com.es2.designpatterns.Credential;

public class CloudStorageFactory implements StorageFactory {
    @Override
    public CredentialStorage createStorage() {
        return new CloudStorage();
    }
}
