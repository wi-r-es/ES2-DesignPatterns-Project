package src.com.es2.designpatterns.Credential;


import src.com.es2.designpatterns.Credential.Generator.PasswordGenerator;

import java.util.UUID;

public class CredentialFactory {
    // The single instance
    private static CredentialFactory instance;
    private PasswordGenerator passwordGenerator;
    //Reference to SecurityCriteriaBuilder
    private SecurityCriteriaBuilderProvider builderProvider;

    // Private constructor
    private CredentialFactory() {
        this.passwordGenerator = new PasswordGenerator();;
        this.builderProvider = new DefaultSecurityCriteriaBuilderProvider();
    }
    // Global access point
    public static synchronized CredentialFactory getInstance() {
        if (instance == null) {
            instance = new CredentialFactory();
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("Singleton instance: " + this);
    }

    // Default implementation
    private class DefaultSecurityCriteriaBuilderProvider implements SecurityCriteriaBuilderProvider {
        @Override
        public SecurityCriteria.Builder getBuilderFor(CredentialType type) {
            SecurityCriteria.Builder builder = new SecurityCriteria.Builder();
            
            // Pre-configure the builder based on credential type
            switch (type) {
                case API_KEY:
                    return builder.includeSymbols(false).algorithm("standard");
                case SECRET_KEY:
                    return builder.algorithm("enhanced");
                case PIN:
                    return builder.length(6).includeSymbols(false).algorithm("pin");
                default:
                    return builder; // Default configuration
            }
        }
    }


    

    

    public Credential createCredential(CredentialType type) {
        String id = UUID.randomUUID().toString();
        SecurityCriteria criteria = builderProvider.getBuilderFor(type).build();
        
        switch (type) {
            case PASSWORD:
                return new Credential(id, "Password", passwordGenerator.generatePassword(criteria));
            case API_KEY:
                return new Credential(id, "API Key", passwordGenerator.generatePassword(criteria));
            case SECRET_KEY:
                return new Credential(id, "Secret Key", passwordGenerator.generatePassword(criteria));
            case PIN:
                return new Credential(id, "PIN", passwordGenerator.generatePassword(criteria));
            case CREDIT_CARD:
                Credential ccCredential = new Credential(id, "Credit Card", "");
                ccCredential.setMetadata("type", "cc");
                return ccCredential;
            default:
                throw new IllegalArgumentException("Unsupported credential type: " + type);
        }
    }

    // Method to register custom algorithms @DEPRECATED
    // public void registerAlgorithm(String name, GenerationAlgorithm algorithm) {
    //     passwordGenerator.registerAlgorithm(name, algorithm);
    // }
}