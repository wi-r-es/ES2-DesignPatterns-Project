package src.com.es2.designpatterns.Credential;

public interface CredentialStorage {

    void saveCredential(Credential credential);
    Credential loadCredential(String id);

}