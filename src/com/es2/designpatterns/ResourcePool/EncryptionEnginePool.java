package src.com.es2.designpatterns.ResourcePool;

/**
 * A pool of encryption engines that can be reused.
 * This class is a specialized wrapper around the generic ObjectPool for EncryptionEngines.
 */
public class EncryptionEnginePool {
    // The underlying object pool
    private final ObjectPool<EncryptionEngine> pool;
    
    // Encryption algorithm for this pool
    private final String algorithm;
    
    // Key size in bits
    private final int keySize;
    
    /**
     * Creates a new encryption engine pool with the specified maximum size.
     *
     * @param maxEngines The maximum number of engines in the pool
     * @param algorithm The encryption algorithm to use
     * @param keySize The key size in bits
     */
    public EncryptionEnginePool(int maxEngines, String algorithm, int keySize) {
        this.algorithm = algorithm;
        this.keySize = keySize;
        
        // Create the object pool with a factory for encryption engines
        this.pool = new ObjectPool<>(maxEngines, 
            () -> new EncryptionEngine(algorithm, keySize));
    }
    
    /**
     * Gets an encryption engine from the pool.
     *
     * @return An encryption engine
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    public EncryptionEngine getEngine() throws InterruptedException {
        return pool.acquire();
    }
    
    /**
     * Gets an encryption engine from the pool with a timeout.
     *
     * @param timeoutMillis The maximum time to wait in milliseconds
     * @return An encryption engine, or null if the timeout expires
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    public EncryptionEngine getEngine(long timeoutMillis) throws InterruptedException {
        return pool.acquire(timeoutMillis);
    }
    
    /**
     * Releases an encryption engine back to the pool.
     *
     * @param engine The engine to release
     */
    public void releaseEngine(EncryptionEngine engine) {
        pool.release(engine);
    }
    
    /**
     * Gets the number of available engines in the pool.
     *
     * @return The number of available engines
     */
    public int getAvailableCount() {
        return pool.getAvailableCount();
    }
    
    /**
     * Gets the number of engines currently in use.
     *
     * @return The number of engines in use
     */
    public int getInUseCount() {
        return pool.getInUseCount();
    }
    
    /**
     * Gets the total number of engines managed by the pool.
     *
     * @return The total number of engines
     */
    public int getTotalCount() {
        return pool.getTotalCount();
    }
    
    /**
     * Gets the encryption algorithm for this pool.
     *
     * @return The encryption algorithm
     */
    public String getAlgorithm() {
        return algorithm;
    }
    
    /**
     * Gets the key size for engines in this pool.
     *
     * @return The key size in bits
     */
    public int getKeySize() {
        return keySize;
    }
}