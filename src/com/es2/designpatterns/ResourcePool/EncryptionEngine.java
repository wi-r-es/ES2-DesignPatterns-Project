package src.com.es2.designpatterns.ResourcePool;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Represents an encryption engine for securing credentials.
 * This is another example of a resource-intensive object that benefits from pooling.
 */
public class EncryptionEngine {
    // The encryption algorithm (e.g., "AES", "DES")
    private final String algorithm;
    
    // The key size in bits
    private final int keySize;
    
    // Secure random number generator
    private final SecureRandom random;
    
    // Simulated initialization time to demonstrate resource cost
    private static final long INIT_TIME_MS = 800;
    
    /**
     * Creates a new encryption engine with the specified algorithm and key size.
     *
     * @param algorithm The encryption algorithm to use
     * @param keySize The key size in bits
     */
    public EncryptionEngine(String algorithm, int keySize) {
        this.algorithm = algorithm;
        this.keySize = keySize;
        this.random = new SecureRandom();
        
        // Simulate expensive initialization
        simulateInitialization();
    }
    
    /**
     * Simulates the time-consuming process of initializing the encryption engine.
     */
    private void simulateInitialization() {
        System.out.println("Initializing encryption engine (" + algorithm + "-" + keySize + ")...");
        
        try {
            // Simulate time-consuming initialization
            Thread.sleep(INIT_TIME_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Encryption engine initialized (" + algorithm + "-" + keySize + ")");
    }
    
    /**
     * Encrypts the given data using the specified key.
     *
     * @param data The data to encrypt
     * @param key The encryption key
     * @return The encrypted data, encoded in Base64
     */
    public String encrypt(String data, String key) {
        try {
            // Simulate encryption process
            Thread.sleep(50);
            
            // Convert the key to bytes and create a SecretKeySpec
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);
            keyBytes = java.util.Arrays.copyOf(keyBytes, keySize / 8);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, algorithm);
            
            // Initialize the cipher for encryption
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            // Encrypt the data
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // Encode the encrypted bytes in Base64
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.err.println("Encryption error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Decrypts the given data using the specified key.
     *
     * @param encryptedData The encrypted data, encoded in Base64
     * @param key The encryption key
     * @return The decrypted data
     */
    public String decrypt(String encryptedData, String key) {
        try {
            // Simulate decryption process
            Thread.sleep(50);
            
            // Convert the key to bytes and create a SecretKeySpec
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);
            keyBytes = java.util.Arrays.copyOf(keyBytes, keySize / 8);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, algorithm);
            
            // Initialize the cipher for decryption
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            // Decode the Base64 encoded data
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            
            // Decrypt the data
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Decryption error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Generates a new random encryption key.
     *
     * @return A Base64 encoded encryption key
     */
    public String generateKey() {
        try {
            // Simulate key generation process
            Thread.sleep(30);
            
            // Generate a random key
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
            keyGen.init(keySize, random);
            SecretKey secretKey = keyGen.generateKey();
            
            // Encode the key in Base64
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            System.err.println("Key generation error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Gets the encryption algorithm used by this engine.
     *
     * @return The encryption algorithm
     */
    public String getAlgorithm() {
        return algorithm;
    }
    
    /**
     * Gets the key size used by this engine.
     *
     * @return The key size in bits
     */
    public int getKeySize() {
        return keySize;
    }
}