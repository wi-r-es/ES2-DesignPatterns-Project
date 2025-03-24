package src.com.es2.designpatterns.Credential;

import java.util.HashMap;
import java.util.Map;

public class DatabaseStorage implements CredentialStorage{

    private Map<String, Credential> database = new HashMap<>();

    @Override
    public void saveCredential(Credential credential) {
        database.put(credential.getId(), credential);
        System.out.println("Credencial salva na base de dados: " + credential);
    }

    @Override
    public Credential loadCredential(String id) {
        return database.get(id);
    }

}
