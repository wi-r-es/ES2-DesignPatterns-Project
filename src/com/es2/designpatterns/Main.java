package src.com.es2.designpatterns;

import src.com.es2.designpatterns.Configuration.ConfigurationManager;

public class Main {
    
    public static void main(String[] args) {
        ConfigurationManager configManager = ConfigurationManager.getInstance();
        int maxLength = configManager.getConfiguration("maxPasswordLength");
        String storageType = configManager.getConfiguration("defaultStorageType");

        System.out.println("Configuration Manager Settings. MaxLength:" + maxLength + " StorageType: " + storageType  );

        configManager.setConfiguration("defaultStorageType", "CLOUD");
        configManager.setConfiguration("maxPasswordLength", 64);

        System.out.println("Configuration Manager Settings. MaxLength:" + configManager.getConfiguration("maxPasswordLength") + " StorageType: " + configManager.getConfiguration("defaultStorageType")  );
    }
}
