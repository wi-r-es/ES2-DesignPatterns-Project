package src.com.es2.designpatterns.ResourcePool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;

/**
 * A generic object pool implementation that allows reusing expensive resources.
 * This class is thread-safe and supports configurable pool sizes and timeout-based
 * resource acquisition.
 *
 * @param <T> The type of object to pool
 */
public class ObjectPool<T> {
    // Queue of available objects
    private final Queue<T> available;
    
    // Set of objects currently in use
    private final Set<T> inUse;
    
    // Maximum number of objects the pool can contain
    @SuppressWarnings("unused")
    private final int maxObjects;
    
    // Function to create new objects
    private final Supplier<T> objectFactory;
    
    // Semaphore to control access to the pool
    private final Semaphore semaphore;
    
    /**
     * Creates a new object pool with the specified maximum size and object factory.
     *
     * @param maxObjects The maximum number of objects the pool can contain
     * @param objectFactory A supplier function that creates new objects
     */
    public ObjectPool(int maxObjects, Supplier<T> objectFactory) {
        this.available = new LinkedList<>();
        this.inUse = new HashSet<>();
        this.maxObjects = maxObjects;
        this.objectFactory = objectFactory;
        this.semaphore = new Semaphore(maxObjects, true); // Fair semaphore
    }
    
    /**
     * Acquires an object from the pool. If no objects are available and the pool
     * hasn't reached its maximum size, a new object will be created.
     *
     * @return An object from the pool
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    public T acquire() throws InterruptedException {
        // Wait until a permit is available
        semaphore.acquire();
        
        synchronized (this) {
            T object;
            
            if (!available.isEmpty()) {
                // Reuse an existing object from the pool
                object = available.poll();
            } else {
                // Create a new object
                object = objectFactory.get();
            }
            
            // Mark the object as in use
            inUse.add(object);
            
            return object;
        }
    }
    
    /**
     * Acquires an object from the pool with a timeout.
     *
     * @param timeoutMillis The maximum time to wait in milliseconds
     * @return An object from the pool, or null if the timeout expires
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    public T acquire(long timeoutMillis) throws InterruptedException {
        // Wait until a permit is available or timeout occurs
        if (!semaphore.tryAcquire(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)) {
            return null; // Timeout expired
        }
        
        synchronized (this) {
            T object;
            
            if (!available.isEmpty()) {
                // Reuse an existing object from the pool
                object = available.poll();
            } else {
                // Create a new object
                object = objectFactory.get();
            }
            
            // Mark the object as in use
            inUse.add(object);
            
            return object;
        }
    }
    
    /**
     * Releases an object back to the pool.
     *
     * @param object The object to release
     * @throws IllegalArgumentException If the object was not acquired from this pool
     */
    public synchronized void release(T object) {
        if (!inUse.remove(object)) {
            throw new IllegalArgumentException("Object was not acquired from this pool");
        }
        
        // Add the object back to the available queue
        available.offer(object);
        
        // Release a permit
        semaphore.release();
    }
    
    /**
     * Gets the number of objects currently available in the pool.
     *
     * @return The number of available objects
     */
    public synchronized int getAvailableCount() {
        return available.size();
    }
    
    /**
     * Gets the number of objects currently in use.
     *
     * @return The number of objects in use
     */
    public synchronized int getInUseCount() {
        return inUse.size();
    }
    
    /**
     * Gets the total number of objects managed by the pool.
     *
     * @return The total number of objects
     */
    public synchronized int getTotalCount() {
        return available.size() + inUse.size();
    }
    
    /**
     * Closes all objects in the pool. This should be called when the pool
     * is no longer needed to free resources.
     *
     * @param closer A function that closes/cleans up an object
     */
    public synchronized void close(java.util.function.Consumer<T> closer) {
        // Close all available objects
        for (T object : available) {
            closer.accept(object);
        }
        
        // Clear the available queue
        available.clear();
        
        // Note: We don't close objects that are in use, as they might still be needed
        // The caller is responsible for releasing all objects before closing the pool
    }
}