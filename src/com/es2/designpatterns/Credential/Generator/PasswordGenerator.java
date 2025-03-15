package src.com.es2.designpatterns.Credential.Generator;


import src.com.es2.designpatterns.Credential.SecurityCriteria;

import java.util.HashMap;
import java.util.Map;

public class PasswordGenerator {
    private Map<String, GenerationAlgorithm> algorithms;
    
    public PasswordGenerator() {
        algorithms = new HashMap<>();
        // Register default algorithms
        algorithms.put("standard", new StandardAlgorithm());
        algorithms.put("enhanced", new EnhancedAlgorithm());
    }
    
    public String generatePassword(SecurityCriteria criteria) {
        String algorithmName = criteria.getAlgorithm();
        GenerationAlgorithm algorithm = algorithms.get(algorithmName);
        
        if (algorithm == null) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
        }
        
        return algorithm.generate(criteria);
    }
    
    public void registerAlgorithm(String name, GenerationAlgorithm algorithm) {
        algorithms.put(name, algorithm);
    }
}