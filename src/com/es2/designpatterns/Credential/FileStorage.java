package src.com.es2.designpatterns.Credential;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileStorage implements CredentialStorage{

    private static final String FILE_NAME = "credentials.dat";
    private Map<String, Credential> storage = new HashMap<>();

    public FileStorage() {
        loadFromFile();
    }

    @Override
    public void saveCredential(Credential credential) {
        storage.put(credential.getId(), credential);
        saveToFile();
    }

    @Override
    public Credential loadCredential(String id) {
        return storage.get(id);
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            storage = (Map<String, Credential>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            storage = new HashMap<>();
        }
    }
}
