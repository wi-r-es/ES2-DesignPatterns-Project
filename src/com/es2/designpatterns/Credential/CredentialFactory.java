package src.com.es2.designpatterns.Credential;


import src.com.es2.designpatterns.Credential.Generator.GenerationAlgorithm;
import src.com.es2.designpatterns.Credential.Generator.PasswordGenerator;

import java.util.UUID;

public class CredentialFactory {
    // The single instance
    private static CredentialFactory instance;
    private PasswordGenerator passwordGenerator;
    private CredentialStorage storage;

    // Private constructor
    private CredentialFactory(StorageFactory storageFactory) {
        this.passwordGenerator = new PasswordGenerator();
        this.storage = storageFactory.createStorage();
    }

    // Global access point
    public static synchronized CredentialFactory getInstance(StorageFactory storageFactory) {
        if (instance == null) {
            instance = new CredentialFactory(storageFactory);
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("Singleton instance: " + this);
    }

    public Credential createCredential(CredentialType type, SecurityCriteria criteria) {
        String id = UUID.randomUUID().toString();
        Credential credential;

        switch (type) {
            case PASSWORD:
                String password = passwordGenerator.generatePassword(criteria);
                return credential = new Credential(id, "Password", password);

            case API_KEY:
                // API keys might have different requirements
                SecurityCriteria apiCriteria = new SecurityCriteria.Builder()
                        .includeSymbols(false)
                        .algorithm("standard")
                        .build();
                String apiKey = passwordGenerator.generatePassword(apiCriteria);
                Credential apiCredential = new Credential(id, "API Key", apiKey);
                apiCredential.setMetadata("type", "api");
                return apiCredential;

            case SECRET_KEY:
                // Secret keys might be longer and more complex
                SecurityCriteria secretCriteria = new SecurityCriteria.Builder()
                        .algorithm("enhanced")
                        .build();
                String secretKey = passwordGenerator.generatePassword(secretCriteria);
                Credential secretCredential = new Credential(id, "Secret Key", secretKey);
                secretCredential.setMetadata("type", "secret");
                return secretCredential;

            case CREDIT_CARD:
                // Credit card credentials would store card details
                Credential ccCredential = new Credential(id, "Credit Card", "");
                ccCredential.setMetadata("type", "cc");
                return ccCredential;

            case PIN:
                SecurityCriteria pinCriteria = new SecurityCriteria.Builder()
                        .length(4)
                        .includeSymbols(false)
                        .algorithm("pin")
                        .build();
                String pin = passwordGenerator.generatePassword(pinCriteria);
                Credential pinCredential = new Credential(id, "PIN", pin);
                pinCredential.setMetadata("type", "pin");
                return pinCredential;

            default:
                throw new IllegalArgumentException("Unsupported credential type: " + type);
        }
    }

    // Method to register custom algorithms
    public void registerAlgorithm(String name, GenerationAlgorithm algorithm) {
        passwordGenerator.registerAlgorithm(name, algorithm);
    }
}