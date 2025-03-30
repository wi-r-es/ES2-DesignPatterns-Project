package src.com.es2.designpatterns.Storage;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Storage.Types.CloudStorage;
import src.com.es2.designpatterns.Storage.Types.DatabaseStorage;
import src.com.es2.designpatterns.Storage.Types.FileStorage;

public class StorageFactory {

    Storage CloudStorage = new CloudStorage();
    Storage DatabaseStorage = new DatabaseStorage();
    Storage FileStorage = new FileStorage();
    String defaultType;

    public StorageFactory(String type) {
        this.defaultType = type;
    }

    public void createStorage(Credential credential, String type) {
        if (type == null) {
            type = defaultType;
        }
        switch (type) {
            case "CLOUD":
                CloudStorage.saveCredential(credential);
                break;
            case "DATABASE":
                DatabaseStorage.saveCredential(credential);
                break;
            case "FILE":
                FileStorage.saveCredential(credential);
                break;
            default:
                throw new IllegalArgumentException("Storage type not recognized");
            }
    }

    public void printCredential(StorageType type, String id) {
        switch (type)
        {
            case CLOUD:
                Credential Cloudcredential = CloudStorage.retrieveCredential(id);
                if (Cloudcredential != null) {
                    System.out.println("Credential ID: " + Cloudcredential.getId() +
                            " | Credential: " + Cloudcredential.getName() +
                            " | Stored in: " + CloudStorage.getStorageType());
                } else {
                    System.out.println("Credential ID: " + id + " not found in " + CloudStorage.getStorageType());
                }
                break;
            case DATABASE:
                Credential Databasecredential = DatabaseStorage.retrieveCredential(id);
                if (Databasecredential != null) {
                    System.out.println("Credential ID: " + Databasecredential.getId() +
                            " | Credential: " + Databasecredential.getName() +
                            " | Stored in: " + DatabaseStorage.getStorageType());
                } else {
                    System.out.println("Credential ID: " + id + " not found in " + DatabaseStorage.getStorageType());
                }
                break;
            case FILE:
                Credential FileCredential = FileStorage.retrieveCredential(id);
                if (FileCredential != null) {
                    System.out.println("Credential ID: " + FileCredential.getId() +
                            " | Credential: " + FileCredential.getName() +
                            " | Stored in: " + FileStorage.getStorageType());
                } else {
                    System.out.println("Credential ID: " + id + " not found in " + FileStorage.getStorageType());
                }
                break;
        }


    }
}
