package src.com.es2.designpatterns.Credential;


import src.com.es2.designpatterns.Credential.Generator.GenerationAlgorithm;
import src.com.es2.designpatterns.Credential.Generator.PasswordGenerator;

import java.util.UUID;

public class CredentialFactory {
    // The single instance
    private static CredentialFactory instance;
    private PasswordGenerator passwordGenerator;

    // Private constructor
    private CredentialFactory(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    // Global access point
    public static synchronized CredentialFactory getInstance(PasswordGenerator passwordGenerator) {
        if (instance == null) {
            instance = new CredentialFactory(passwordGenerator);
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("Singleton instance: " + this);
    }

    public Credential createCredential(CredentialType type, SecurityCriteria criteria) {
        String id = UUID.randomUUID().toString();

        switch (type) {
            case PASSWORD:
                return new Credential(id, "Password", passwordGenerator.generatePassword(criteria));

            case API_KEY:
                SecurityCriteria apiCriteria = new SecurityCriteria.Builder()
                        .includeSymbols(false)
                        .algorithm("standard")
                        .build();
                return new Credential(id, "API Key", passwordGenerator.generatePassword(apiCriteria));

            case SECRET_KEY:
                SecurityCriteria secretCriteria = new SecurityCriteria.Builder()
                        .algorithm("enhanced")
                        .build();
                return new Credential(id, "Secret Key", passwordGenerator.generatePassword(secretCriteria));

            case CREDIT_CARD:
                Credential ccCredential = new Credential(id, "Credit Card", "");
                ccCredential.setMetadata("type", "cc");
                return ccCredential;

            case PIN:
                SecurityCriteria pinCriteria = new SecurityCriteria.Builder()
                        .length(4)
                        .includeSymbols(false)
                        .algorithm("pin")
                        .build();
                return new Credential(id, "PIN", passwordGenerator.generatePassword(pinCriteria));

            default:
                throw new IllegalArgumentException("Unsupported credential type: " + type);
        }
    }

    // Method to register custom algorithms
    public void registerAlgorithm(String name, GenerationAlgorithm algorithm) {
        passwordGenerator.registerAlgorithm(name, algorithm);
    }
}