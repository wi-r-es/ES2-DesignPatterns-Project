package src.com.es2.designpatterns.ResourcePool;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages various resource pools for the password manager.
 * This class is a singleton that provides access to connection pools and encryption engine pools.
 */
public class ResourcePoolManager {
    // Singleton instance
    private static ResourcePoolManager instance;
    
    // Map of connection pools by type
    private final Map<String, ConnectionPool> connectionPools;
    
    // Map of encryption engine pools by algorithm
    private final Map<String, EncryptionEnginePool> encryptionPools;
    
    // Configuration
    private final int defaultMaxConnections;
    private final int defaultMaxEngines;
    
    /**
     * Private constructor for singleton pattern.
     */
    private ResourcePoolManager() {
        connectionPools = new HashMap<>();
        encryptionPools = new HashMap<>();
        
        // Initialize with default configuration
        defaultMaxConnections = 5;
        defaultMaxEngines = 3;
        
        // Initialize default pools
        initializeDefaultPools();
    }
    
    /**
     * Gets the singleton instance of the ResourcePoolManager.
     *
     * @return The singleton instance
     */
    public static synchronized ResourcePoolManager getInstance() {
        if (instance == null) {
            instance = new ResourcePoolManager();
        }
        return instance;
    }
    
    /**
     * Initializes the default resource pools.
     */
    private void initializeDefaultPools() {
        // Create default connection pools
        createConnectionPool("FILE", true);
        createConnectionPool("DATABASE", true);
        createConnectionPool("CLOUD", true);
        
        // Create default encryption engine pools
        createEncryptionPool("AES", 256);
        createEncryptionPool("DES", 56);
    }
    
    /**
     * Creates a new connection pool for the specified type.
     *
     * @param connectionType The type of connections in the pool
     * @param encrypted Whether connections should use encryption
     * @return The newly created connection pool
     */
    public ConnectionPool createConnectionPool(String connectionType, boolean encrypted) {
        String poolKey = connectionType + (encrypted ? "_ENCRYPTED" : "");
        
        ConnectionPool pool = new ConnectionPool(defaultMaxConnections, connectionType, encrypted);
        connectionPools.put(poolKey, pool);
        
        return pool;
    }
    
    /**
     * Gets a connection pool for the specified type.
     *
     * @param connectionType The type of connections in the pool
     * @param encrypted Whether connections should use encryption
     * @return The connection pool, or null if not found
     */
    public ConnectionPool getConnectionPool(String connectionType, boolean encrypted) {
        String poolKey = connectionType + (encrypted ? "_ENCRYPTED" : "");
        return connectionPools.get(poolKey);
    }
    
    /**
     * Creates a new encryption engine pool for the specified algorithm.
     *
     * @param algorithm The encryption algorithm
     * @param keySize The key size in bits
     * @return The newly created encryption engine pool
     */
    public EncryptionEnginePool createEncryptionPool(String algorithm, int keySize) {
        String poolKey = algorithm + "_" + keySize;
        
        EncryptionEnginePool pool = new EncryptionEnginePool(defaultMaxEngines, algorithm, keySize);
        encryptionPools.put(poolKey, pool);
        
        return pool;
    }
    
    /**
     * Gets an encryption engine pool for the specified algorithm.
     *
     * @param algorithm The encryption algorithm
     * @param keySize The key size in bits
     * @return The encryption engine pool, or null if not found
     */
    public EncryptionEnginePool getEncryptionPool(String algorithm, int keySize) {
        String poolKey = algorithm + "_" + keySize;
        return encryptionPools.get(poolKey);
    }
    
    /**
     * Gets a secure connection of the specified type.
     *
     * @param connectionType The type of connection
     * @param encrypted Whether the connection should use encryption
     * @return A secure connection
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    public SecureConnection getConnection(String connectionType, boolean encrypted) 
            throws InterruptedException {
        ConnectionPool pool = getConnectionPool(connectionType, encrypted);
        
        if (pool == null) {
            pool = createConnectionPool(connectionType, encrypted);
        }
        
        return pool.getConnection();
    }
    
    /**
     * Releases a secure connection back to its pool.
     *
     * @param connection The connection to release
     */
    public void releaseConnection(SecureConnection connection) {
        ConnectionPool pool = getConnectionPool(connection.getConnectionType(), connection.isEncrypted());
        
        if (pool != null) {
            pool.releaseConnection(connection);
        }
    }
    
    /**
     * Gets an encryption engine of the specified type.
     *
     * @param algorithm The encryption algorithm
     * @param keySize The key size in bits
     * @return An encryption engine
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    public EncryptionEngine getEncryptionEngine(String algorithm, int keySize) 
            throws InterruptedException {
        EncryptionEnginePool pool = getEncryptionPool(algorithm, keySize);
        
        if (pool == null) {
            pool = createEncryptionPool(algorithm, keySize);
        }
        
        return pool.getEngine();
    }
    
    /**
     * Releases an encryption engine back to its pool.
     *
     * @param engine The engine to release
     */
    public void releaseEncryptionEngine(EncryptionEngine engine) {
        EncryptionEnginePool pool = getEncryptionPool(engine.getAlgorithm(), engine.getKeySize());
        
        if (pool != null) {
            pool.releaseEngine(engine);
        }
    }
    
    /**
     * Closes all resources managed by this manager.
     */
    public void closeAll() {
        // Close all connection pools
        for (ConnectionPool pool : connectionPools.values()) {
            pool.closeAll();
        }
    }
}