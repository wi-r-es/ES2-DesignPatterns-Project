package src.com.es2.designpatterns.ResourcePool;

/**
 * A pool of secure connections that can be reused.
 * This class is a specialized wrapper around the generic ObjectPool for SecureConnections.
 */
public class ConnectionPool {
    // The underlying object pool
    private final ObjectPool<SecureConnection> pool;
    
    // Connection type for this pool
    private final String connectionType;
    
    // Whether connections should use encryption
    private final boolean encrypted;
    
    /**
     * Creates a new connection pool with the specified maximum size.
     *
     * @param maxConnections The maximum number of connections in the pool
     * @param connectionType The type of connections to create
     * @param encrypted Whether connections should use encryption
     */
    public ConnectionPool(int maxConnections, String connectionType, boolean encrypted) {
        this.connectionType = connectionType;
        this.encrypted = encrypted;
        
        // Create the object pool with a factory for secure connections
        this.pool = new ObjectPool<>(maxConnections, 
            () -> new SecureConnection(connectionType, encrypted));
    }
    
    /**
     * Gets a connection from the pool.
     *
     * @return A secure connection
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    public SecureConnection getConnection() throws InterruptedException {
        SecureConnection connection = pool.acquire();
        
        // Ensure the connection is open
        if (!connection.isActive()) {
            connection.open();
        }
        
        return connection;
    }
    
    /**
     * Gets a connection from the pool with a timeout.
     *
     * @param timeoutMillis The maximum time to wait in milliseconds
     * @return A secure connection, or null if the timeout expires
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    public SecureConnection getConnection(long timeoutMillis) throws InterruptedException {
        SecureConnection connection = pool.acquire(timeoutMillis);
        
        if (connection != null && !connection.isActive()) {
            connection.open();
        }
        
        return connection;
    }
    
    /**
     * Releases a connection back to the pool.
     *
     * @param connection The connection to release
     */
    public void releaseConnection(SecureConnection connection) {
        // We don't close the connection, just return it to the pool
        pool.release(connection);
    }
    
    /**
     * Closes all connections in the pool.
     */
    public void closeAll() {
        pool.close(connection -> {
            if (connection.isActive()) {
                connection.close();
            }
        });
    }
    
    /**
     * Gets the number of available connections in the pool.
     *
     * @return The number of available connections
     */
    public int getAvailableCount() {
        return pool.getAvailableCount();
    }
    
    /**
     * Gets the number of connections currently in use.
     *
     * @return The number of connections in use
     */
    public int getInUseCount() {
        return pool.getInUseCount();
    }
    
    /**
     * Gets the total number of connections managed by the pool.
     *
     * @return The total number of connections
     */
    public int getTotalCount() {
        return pool.getTotalCount();
    }
    
    /**
     * Gets the connection type for this pool.
     *
     * @return The connection type
     */
    public String getConnectionType() {
        return connectionType;
    }
    
    /**
     * Checks if connections in this pool use encryption.
     *
     * @return true if connections use encryption, false otherwise
     */
    public boolean isEncrypted() {
        return encrypted;
    }
}