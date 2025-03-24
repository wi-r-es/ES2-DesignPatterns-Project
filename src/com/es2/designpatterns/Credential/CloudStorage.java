package src.com.es2.designpatterns.Credential;

public class CloudStorage implements CredentialStorage{

    @Override
    public void saveCredential(Credential credential) {
        System.out.println("Credencial armazenada na Cloud: " + credential);
    }

    @Override
    public Credential loadCredential(String id) {
        System.out.println("Buscando credencial na Cloud...");
        return null;
    }

}
