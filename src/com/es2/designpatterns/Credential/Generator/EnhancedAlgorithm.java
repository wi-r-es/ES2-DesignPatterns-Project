package src.com.es2.designpatterns.Credential.Generator;

import src.com.es2.designpatterns.Credential.SecurityCriteria;

import java.security.SecureRandom;
import java.util.Random;

public class EnhancedAlgorithm implements GenerationAlgorithm {
    @Override
    public String generate(SecurityCriteria criteria) {
        StringBuilder password = new StringBuilder();
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String symbolChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        StringBuilder validChars = new StringBuilder();

        if (criteria.isIncludeUppercase()) {
            validChars.append(uppercaseChars);
        }

        if (criteria.isIncludeLowercase()) {
            validChars.append(lowercaseChars);
        }

        if (criteria.isIncludeNumbers()) {
            validChars.append(numberChars);
        }

        if (criteria.isIncludeSymbols()) {
            validChars.append(symbolChars);
        }

        // Remove excluded characters
        String excluded = criteria.getExcludedChars();
        for (int i = 0; i < excluded.length(); i++) {
            char c = excluded.charAt(i);
            int index;
            while ((index = validChars.indexOf(String.valueOf(c))) != -1) {
                validChars.deleteCharAt(index);
            }
        }

        if (validChars.length() == 0) {
            throw new IllegalArgumentException("No valid characters available for password generation");
        }

        // Generate the password
        Random random = new SecureRandom();
        for (int i = 0; i < criteria.getLength(); i++) {
            int randomIndex = random.nextInt(validChars.length());
            password.append(validChars.charAt(randomIndex));
        }

        return password.toString();
    }
}