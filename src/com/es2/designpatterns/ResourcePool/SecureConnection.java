package src.com.es2.designpatterns.ResourcePool;

import java.util.UUID;

/**
 * Represents a secure connection for password storage and retrieval.
 * This is an example of a resource-intensive object that benefits from pooling.
 */
public class SecureConnection {
    // Unique identifier for this connection
    private final String connectionId;
    
    // Type of connection (e.g., "FILE", "DATABASE", "CLOUD")
    private final String connectionType;
    
    // Whether the connection uses encryption
    private final boolean encrypted;
    
    // Whether the connection is currently active
    private boolean active;
    
    // Simulated initialization time to demonstrate resource cost
    private static final long INIT_TIME_MS = 500;
    
    /**
     * Creates a new secure connection of the specified type.
     *
     * @param connectionType The type of connection
     * @param encrypted Whether the connection should use encryption
     */
    public SecureConnection(String connectionType, boolean encrypted) {
        this.connectionId = UUID.randomUUID().toString();
        this.connectionType = connectionType;
        this.encrypted = encrypted;
        this.active = false;
        
        // Simulate expensive initialization
        simulateInitialization();
    }
    
    /**
     * Simulates the time-consuming process of establishing a secure connection.
     */
    private void simulateInitialization() {
        System.out.println("Initializing secure connection of type " + connectionType + 
                          " (ID: " + connectionId + ")...");
        
        try {
            // Simulate time-consuming initialization
            Thread.sleep(INIT_TIME_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Secure connection initialized (ID: " + connectionId + ")");
    }
    
    /**
     * Opens the connection.
     *
     * @return true if the connection was opened successfully, false otherwise
     */
    public boolean open() {
        if (active) {
            return true; // Already open
        }
        
        System.out.println("Opening secure connection (ID: " + connectionId + ")");
        
        // Simulate connection process
        try {
            Thread.sleep(100);
            active = true;
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    /**
     * Closes the connection.
     *
     * @return true if the connection was closed successfully, false otherwise
     */
    public boolean close() {
        if (!active) {
            return true; // Already closed
        }
        
        System.out.println("Closing secure connection (ID: " + connectionId + ")");
        
        // Simulate closing process
        try {
            Thread.sleep(50);
            active = false;
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    /**
     * Checks if the connection is currently active.
     *
     * @return true if the connection is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Sends data over the connection.
     *
     * @param data The data to send
     * @return true if the data was sent successfully, false otherwise
     */
    public boolean send(String data) {
        if (!active) {
            System.out.println("Cannot send data: connection is not active");
            return false;
        }
        
        System.out.println("Sending data over secure connection (ID: " + connectionId + "): " +
                          (encrypted ? "[ENCRYPTED]" : "") + 
                          data.substring(0, Math.min(10, data.length())) + "...");
        
        // Simulate sending process
        try {
            Thread.sleep(30);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    /**
     * Receives data over the connection.
     *
     * @return The received data, or null if an error occurred
     */
    public String receive() {
        if (!active) {
            System.out.println("Cannot receive data: connection is not active");
            return null;
        }
        
        System.out.println("Receiving data from secure connection (ID: " + connectionId + ")");
        
        // Simulate receiving process
        try {
            Thread.sleep(20);
            String data = "Received data " + System.currentTimeMillis();
            System.out.println("Received: " + (encrypted ? "[ENCRYPTED]" : "") + 
                              data.substring(0, Math.min(10, data.length())) + "...");
            return data;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    /**
     * Gets the unique identifier for this connection.
     *
     * @return The connection ID
     */
    public String getConnectionId() {
        return connectionId;
    }
    
    /**
     * Gets the type of this connection.
     *
     * @return The connection type
     */
    public String getConnectionType() {
        return connectionType;
    }
    
    /**
     * Checks if this connection uses encryption.
     *
     * @return true if the connection uses encryption, false otherwise
     */
    public boolean isEncrypted() {
        return encrypted;
    }
}