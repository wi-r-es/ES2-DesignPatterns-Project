package src.com.es2.designpatterns;

import src.com.es2.designpatterns.ResourcePool.EncryptionEngine;
import src.com.es2.designpatterns.ResourcePool.ResourcePoolManager;
import src.com.es2.designpatterns.ResourcePool.SecureConnection;

public class ResourcePoolTest {
    public static void main(String[] args) {
        System.out.println("===== Testing Resource Pool Pattern =====\n");
        
        // Get the resource pool manager
        ResourcePoolManager manager = ResourcePoolManager.getInstance();
        
        try {
            // Test secure connections
            testSecureConnections(manager);
            
            // Test encryption engines
            testEncryptionEngines(manager);
            
            // Test resource reuse
            testResourceReuse(manager);
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            // Make sure to close all resources
            manager.closeAll();
        }
    }
    
    /**
     * Tests the secure connection pooling functionality.
     */
    protected static void testSecureConnections(ResourcePoolManager manager) throws InterruptedException {
        System.out.println("\n--- Testing Secure Connections ---");
        
        // Get connections of different types
        System.out.println("Getting FILE connection...");
        SecureConnection fileConn = manager.getConnection("FILE", true);
        
        System.out.println("Getting DATABASE connection...");
        SecureConnection dbConn = manager.getConnection("DATABASE", true);
        
        // Use the connections
        fileConn.send("Sample file data");
        dbConn.send("Sample database query");
        
        // Release the connections
        System.out.println("Releasing connections...");
        manager.releaseConnection(fileConn);
        manager.releaseConnection(dbConn);
        
        System.out.println("Connection test completed.");
    }
    
    /**
     * Tests the encryption engine pooling functionality.
     */
    protected static void testEncryptionEngines(ResourcePoolManager manager) throws InterruptedException {
        System.out.println("\n--- Testing Encryption Engines ---");
        
        // Get encryption engines of different types
        System.out.println("Getting AES engine...");
        EncryptionEngine aesEngine = manager.getEncryptionEngine("AES", 256);
        
        // Generate a key and encrypt/decrypt some data
        String key = aesEngine.generateKey();
        System.out.println("Generated key: " + key);
        
        String originalText = "This is a secret password!";
        System.out.println("Original text: " + originalText);
        
        String encrypted = aesEngine.encrypt(originalText, key);
        System.out.println("Encrypted text: " + encrypted);
        
        String decrypted = aesEngine.decrypt(encrypted, key);
        System.out.println("Decrypted text: " + decrypted);
        
        // Release the engine
        System.out.println("Releasing encryption engine...");
        manager.releaseEncryptionEngine(aesEngine);
        
        System.out.println("Encryption test completed.");
    }
    
    /**
     * Tests that resources are actually reused from the pool.
     */
    protected static void testResourceReuse(ResourcePoolManager manager) throws InterruptedException {
        System.out.println("\n--- Testing Resource Reuse ---");
        
        // Get a connection
        System.out.println("Getting first FILE connection...");
        SecureConnection conn1 = manager.getConnection("FILE", true);
        
        // Use and release the connection
        conn1.send("Test data for connection 1");
        System.out.println("Releasing first connection...");
        manager.releaseConnection(conn1);
        
        // Get another connection of the same type
        System.out.println("Getting second FILE connection (should reuse the first)...");
        SecureConnection conn2 = manager.getConnection("FILE", true);
        
        // This should reuse the same connection
        System.out.println("First connection ID: " + conn1.getConnectionId());
        System.out.println("Second connection ID: " + conn2.getConnectionId());
        System.out.println("Connections are the same object: " + (conn1 == conn2));
        
        // Use and release the second connection
        conn2.send("Test data for connection 2");
        System.out.println("Releasing second connection...");
        manager.releaseConnection(conn2);
        
        // Test parallel usage - get multiple connections at once
        System.out.println("\nTesting parallel usage...");
        System.out.println("Getting 3 FILE connections simultaneously...");
        
        SecureConnection[] connections = new SecureConnection[3];
        for (int i = 0; i < connections.length; i++) {
            connections[i] = manager.getConnection("FILE", true);
            System.out.println("Got connection " + (i + 1) + " with ID: " + connections[i].getConnectionId());
        }
        
        // Release all connections
        System.out.println("Releasing all connections...");
        for (SecureConnection conn : connections) {
            manager.releaseConnection(conn);
        }
        
        System.out.println("Resource reuse test completed.");
    }
}