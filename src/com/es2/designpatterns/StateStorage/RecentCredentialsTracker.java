package src.com.es2.designpatterns.StateStorage;

import src.com.es2.designpatterns.Credential.Credential;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks recently accessed credentials. This class uses the StateManager
 * to persist its state between application sessions.
 */
public class RecentCredentialsTracker implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Singleton instance
    private static RecentCredentialsTracker instance;
    
    // Maximum number of recent credentials to track
    private final int maxRecentCredentials;
    
    // Map of credential IDs to access timestamps (most recent first)
    private Map<String, Date> recentCredentials;
    
    // Reference to the state manager
    private transient IStateManager stateManager; // reference to another object that shouldn't be serialized
    
    // Key for storing in the state manager
    private static final String STATE_KEY = "recentCredentials";
    
    /**
     * Private constructor for singleton pattern.
     */
    @SuppressWarnings("unchecked")
    private RecentCredentialsTracker() {
        this(StateManager.getInstance());
    }
    
    /**
     * Private constructor with dependency injection for testing.
     * 
     * @param stateManager The state manager to use
     */
    @SuppressWarnings("unchecked")
    private RecentCredentialsTracker(IStateManager stateManager) {
        this.maxRecentCredentials = 20;
        this.stateManager = stateManager;
        
        // Try to load from state manager
        Map<String, Date> saved = stateManager.getState(STATE_KEY);
        
        if (saved != null) {
            this.recentCredentials = saved;
        } else {
            this.recentCredentials = new LinkedHashMap<String, Date>(maxRecentCredentials, 0.75f, true) {
                private static final long serialVersionUID = 1L;
                
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Date> eldest) {
                    return size() > maxRecentCredentials;
                }
            };
        }
    }
    
    /**
     * Gets the singleton instance of the RecentCredentialsTracker.
     * 
     * @return The singleton instance
     */
    public static synchronized RecentCredentialsTracker getInstance() {
        if (instance == null) {
            instance = new RecentCredentialsTracker();
        }
        return instance;
    }
    
    /**
     * Gets the singleton instance with a custom state manager (useful for testing).
     * 
     * @param stateManager The state manager to use
     * @return The singleton instance
     */
    public static synchronized RecentCredentialsTracker getInstance(IStateManager stateManager) {
        if (instance == null) {
            instance = new RecentCredentialsTracker(stateManager);
        }
        return instance;
    }
    
    /**
     * Records that a credential was accessed.
     * 
     * @param credential The credential that was accessed
     */
    public void trackCredentialAccess(Credential credential) {
        if (credential != null) {
            recentCredentials.put(credential.getId(), new Date());
            saveState();
        }
    }
    
    /**
     * Gets the IDs of recently accessed credentials, most recent first.
     * 
     * @return A list of credential IDs
     */
    public List<String> getRecentCredentialIds() {
        // Convert the keyset to a list
        List<String> ids = new ArrayList<>(recentCredentials.keySet());
        
        // Sort by timestamp (most recent first)
        ids.sort((id1, id2) -> recentCredentials.get(id2).compareTo(recentCredentials.get(id1)));
        
        return ids;
    }
    
    /**
     * Gets the access timestamp for a credential.
     * 
     * @param credentialId The credential ID
     * @return The access timestamp, or null if not found
     */
    public Date getAccessTimestamp(String credentialId) {
        return recentCredentials.get(credentialId);
    }
    
    /**
     * Clears the recent credentials history.
     */
    public void clearHistory() {
        recentCredentials.clear();
        saveState();
    }
    
    /**
     * Removes a credential from the recent history.
     * 
     * @param credentialId The credential ID to remove
     */
    public void removeCredential(String credentialId) {
        recentCredentials.remove(credentialId);
        saveState();
    }
    
    /**
     * Saves the current state to the state manager.
     */
    private void saveState() {
        stateManager.setState(STATE_KEY, recentCredentials);
        stateManager.saveState();
    }
    
    /**
     * Gets the number of recent credentials being tracked.
     * 
     * @return The number of recent credentials
     */
    public int getRecentCredentialsCount() {
        return recentCredentials.size();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Write the serializable fields
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Read the serializable fields
        
        // Restore the transient stateManager after deserialization
        // We use a static method to get access to it without creating circular dependencies
        stateManager = StateManager.getInstance();
    }
}