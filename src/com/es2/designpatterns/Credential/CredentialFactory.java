package src.com.es2.designpatterns.Credential;


public class CredentialFactory {
    // The single instance
    private static CredentialFactory instance;
    
    private PasswordGenerator passwordGenerator;
    
    // Private constructor
    private CredentialFactory() {
        this.passwordGenerator = new PasswordGenerator();
    }
    
    // Global access point
    public static synchronized CredentialFactory getInstance() {
        if (instance == null) {
            instance = new CredentialFactory();
        }
        return instance;
    }
    
    public Credential createCredential(CredentialType type, SecurityCriteria criteria) {
        String id = UUID.randomUUID().toString();
        
        switch (type) {
            case PASSWORD:
                String password = passwordGenerator.generatePassword(criteria);
                return new Credential(id, "Password", password);
                
            case API_KEY:
                // API keys might have different requirements
                SecurityCriteria apiCriteria = new SecurityCriteria.Builder()
                    .length(32)
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
                    .length(64)
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
                
            default:
                throw new IllegalArgumentException("Unsupported credential type: " + type);
        }
    }
    
    // Method to register custom algorithms
    public void registerAlgorithm(String name, GenerationAlgorithm algorithm) {
        passwordGenerator.registerAlgorithm(name, algorithm);
    }
}
