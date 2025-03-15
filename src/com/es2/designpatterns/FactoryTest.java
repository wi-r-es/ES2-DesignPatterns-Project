import src.com.es2.designpatterns.Configuration.ConfigurationManager;


public class CredentialFactoryTest {
    public static void main(String[] args) {
        // Get the singleton instance
        CredentialFactory factory = CredentialFactory.getInstance();
        
        System.out.println("===== Testing Credential Factory =====");
        
        // Test password generation with different criteria
        System.out.println("\n--- Password Generation ---");
        
        // Standard password
        SecurityCriteria standardCriteria = new SecurityCriteria.Builder().build();
        Credential standardPassword = factory.createCredential(CredentialType.PASSWORD, standardCriteria);
        System.out.println("Standard Password: " + standardPassword.getValue());
        
        // Password with custom criteria
        SecurityCriteria customCriteria = new SecurityCriteria.Builder()
                .length(20)
                .includeSymbols(false)
                .excludedChars("iloO01")  // Exclude confusing characters
                .build();
        Credential customPassword = factory.createCredential(CredentialType.PASSWORD, customCriteria);
        System.out.println("Custom Password: " + customPassword.getValue());
        
        // Enhanced security password
        SecurityCriteria enhancedCriteria = new SecurityCriteria.Builder()
                .length(24)
                .algorithm("enhanced")
                .build();
        Credential enhancedPassword = factory.createCredential(CredentialType.PASSWORD, enhancedCriteria);
        System.out.println("Enhanced Password: " + enhancedPassword.getValue());
        
        // Test other credential types
        System.out.println("\n--- Other Credential Types ---");
        
        // API Key
        Credential apiKey = factory.createCredential(CredentialType.API_KEY, null);
        System.out.println("API Key: " + apiKey.getValue());
        System.out.println("Type Metadata: " + apiKey.getMetadata("type"));
        
        // Secret Key
        Credential secretKey = factory.createCredential(CredentialType.SECRET_KEY, null);
        System.out.println("Secret Key: " + secretKey.getValue());
        System.out.println("Type Metadata: " + secretKey.getMetadata("type"));
        
        // Test adding a custom algorithm
        System.out.println("\n--- Custom Algorithm ---");
        
        // Register a new algorithm directly on the factory
        factory.registerAlgorithm("pin", new GenerationAlgorithm() {
            @Override
            public String generate(SecurityCriteria criteria) {
                StringBuilder pin = new StringBuilder();
                Random random = new SecureRandom();
                for (int i = 0; i < criteria.getLength(); i++) {
                    pin.append(random.nextInt(10));
                }
                return pin.toString();
            }
        });
        
        // Create a PIN with our new algorithm
        SecurityCriteria pinCriteria = new SecurityCriteria.Builder()
                .length(6)
                .algorithm("pin")
                .build();
        
        Credential pinCredential = factory.createCredential(CredentialType.PASSWORD, pinCriteria);
        System.out.println("PIN: " + pinCredential.getValue());
    }
}