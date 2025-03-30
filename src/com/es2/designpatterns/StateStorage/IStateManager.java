package src.com.es2.designpatterns.StateStorage;

import java.util.List;
import java.util.Map;

/**
 * Interface for the state manager component.
 * Defines operations for high-level state management.
 */
public interface IStateManager {
    /**
     * Sets a value in the application state.
     * 
     * @param key The key for the value
     * @param value The value to store
     */
    void setState(String key, Object value);
    
    /**
     * Gets a value from the application state.
     * 
     * @param key The key for the value
     * @return The stored value, or null if not found
     */
    <T> T getState(String key);
    
    /**
     * Removes a value from the application state.
     * 
     * @param key The key for the value to remove
     */
    void removeState(String key);
    
    /**
     * Saves the current application state as a snapshot.
     */
    void saveState();
    
    /**
     * Restores the application state to the previous snapshot.
     * 
     * @return true if successful, false if there was no previous state
     */
    boolean restorePreviousState();
    
    /**
     * Restores the application state to the latest snapshot.
     * 
     * @return true if successful, false if there was no saved state
     */
    boolean restoreLatestState();
    
    /**
     * Saves the application state to disk.
     * 
     * @return true if successful, false otherwise
     */
    boolean saveStateToDisk();
    
    /**
     * Loads the application state from disk.
     * 
     * @return true if successful, false otherwise
     */
    boolean loadStateFromDisk();
    
    /**
     * Gets a list of all saved state snapshots.
     * 
     * @return A list of all state snapshots, newest first
     */
    List<ApplicationState> getStateHistory();
    
    /**
     * Gets the number of saved state snapshots.
     * 
     * @return The number of snapshots
     */
    int getStateHistorySize();
    
    /**
     * Clears all saved state snapshots.
     */
    void clearStateHistory();
    
    /**
     * Sets multiple state values at once.
     * 
     * @param states A map of key-value pairs to set
     */
    void setMultipleStates(Map<String, Object> states);
    
    /**
     * Creates a checkpoint of the current state with a description.
     * 
     * @param description The description for this checkpoint
     */
    void createCheckpoint(String description);
}