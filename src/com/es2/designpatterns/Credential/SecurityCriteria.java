package src.com.es2.designpatterns.Credential;

import src.com.es2.designpatterns.Configuration.ConfigurationManager;

/**
 *  Implements Builder pattern to ease up construction of the security criteria for password creation to be used for the factory pattern
 *
 * 
 */

public class SecurityCriteria {
    private int length;
    private boolean includeUppercase;
    private boolean includeLowercase;
    private boolean includeNumbers;
    private boolean includeSymbols;
    private String excludedChars;
    private String algorithm;



    public static class Builder { //https://refactoring.guru/design-patterns/builder/java/example
        private SecurityCriteria criteria;
        
        public Builder() {
            ConfigurationManager config = ConfigurationManager.getInstance();
            criteria = new SecurityCriteria();
            // Set defaults
            criteria.length = config.getConfiguration("maxPasswordLength");
            criteria.includeUppercase = true;
            criteria.includeLowercase = true;
            criteria.includeNumbers = true;
            criteria.includeSymbols = true;
            criteria.excludedChars = "";
            criteria.algorithm = "standard" ;
        }
        
        public Builder length(int length) {
            criteria.length = length;
            return this;
        }
        
        public Builder includeUppercase(boolean includeUppercase) {
            criteria.includeUppercase = includeUppercase;
            return this;
        }
        
        public Builder includeLowercase(boolean includeLowercase) {
            criteria.includeLowercase = includeLowercase;
            return this;
        }
        
        public Builder includeNumbers(boolean includeNumbers) {
            criteria.includeNumbers = includeNumbers;
            return this;
        }
        
        public Builder includeSymbols(boolean includeSymbols) {
            criteria.includeSymbols = includeSymbols;
            return this;
        }
        
        public Builder excludedChars(String excludedChars) {
            criteria.excludedChars = excludedChars;
            return this;
        }
        
        public Builder algorithm(String algorithm) {
            criteria.algorithm = algorithm;
            return this;
        }
        
        public SecurityCriteria build() {
            return criteria;
        }

    }
        
        
    // Private constructor - use builder instead
    private SecurityCriteria() {}
    
    public int getLength() {
        return length;
    }
    
    public boolean isIncludeUppercase() {
        return includeUppercase;
    }
    
    public boolean isIncludeLowercase() {
        return includeLowercase;
    }
    
    public boolean isIncludeNumbers() {
        return includeNumbers;
    }
    
    public boolean isIncludeSymbols() {
        return includeSymbols;
    }
    
    public String getExcludedChars() {
        return excludedChars;
    }
    
    public String getAlgorithm() {
        return algorithm;
    }
}