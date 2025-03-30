package src.com.es2.designpatterns.StateStorage;

import java.util.List;
import java.util.Map;

/**
 * Manages the application state, providing high-level functionality for
 * saving, restoring, and persisting state.
 * This class serves as a facade for the Memento pattern implementation.
 */
public class StateManager implements IStateManager {
    // Singleton instance
    private static StateManager instance;
    
    // The originator that creates and restores from mementos
    private final IStateOriginator originator;
    
    // The caretaker that manages the mementos
    private final IStateCaretaker caretaker;
    
    // Track if the state has changed since the last save
    private boolean stateChanged;
    
    // The filename for the persistent state
    private final String persistentStateFilename;
    
    /**
     * Private constructor for singleton pattern.
     */
    private StateManager() {
        this(new StateOriginator(), new StateCaretaker());
    }
    
    /**
     * Private constructor for singleton pattern with dependency injection.
     * 
     * @param originator The state originator to use
     * @param caretaker The state caretaker to use
     */
    private StateManager(IStateOriginator originator, IStateCaretaker caretaker) {
        this.originator = originator;
        this.caretaker = caretaker;
        this.stateChanged = false;
        this.persistentStateFilename = "password_manager_state.dat";
        
        // Try to load the previous state
        loadStateFromDisk();
    }
    
    /**
     * Gets the singleton instance of the StateManager.
     * 
     * @return The singleton instance
     */
    public static synchronized IStateManager getInstance() {
        if (instance == null) {
            instance = new StateManager();
        }
        return instance;
    }
    
    /**
     * Gets the singleton instance with custom components (useful for testing).
     * 
     * @param originator The state originator to use
     * @param caretaker The state caretaker to use
     * @return The singleton instance
     */
    public static synchronized IStateManager getInstance(
            IStateOriginator originator, IStateCaretaker caretaker) {
        // For testing, we create a new instance each time
        return new StateManager(originator, caretaker);
    }
    
    /**
     * Sets a value in the application state.
     * 
     * @param key The key for the value
     * @param value The value to store
     */
    @Override
    public void setState(String key, Object value) {
        originator.setState(key, value);
        stateChanged = true;
    }
    
    /**
     * Gets a value from the application state.
     * 
     * @param key The key for the value
     * @return The stored value, or null if not found
     */
    @Override
    public <T> T getState(String key) {
        return originator.getState(key);
    }
    
    /**
     * Removes a value from the application state.
     * 
     * @param key The key for the value to remove
     */
    @Override
    public void removeState(String key) {
        originator.removeState(key);
        stateChanged = true;
    }
    
    /**
     * Saves the current application state as a snapshot.
     */
    @Override
    public void saveState() {
        if (stateChanged) {
            ApplicationState state = originator.saveToMemento();
            caretaker.addMemento(state);
            stateChanged = false;
            
            // Automatically save to disk
            saveStateToDisk();
        }
    }
    
    /**
     * Restores the application state to the previous snapshot.
     * 
     * @return true if successful, false if there was no previous state
     */
    @Override
    public boolean restorePreviousState() {
        ApplicationState previousState = caretaker.getPreviousMemento();
        
        if (previousState != null) {
            originator.restoreFromMemento(previousState);
            stateChanged = false;
            return true;
        }
        
        return false;
    }
    
    /**
     * Restores the application state to the latest snapshot.
     * 
     * @return true if successful, false if there was no saved state
     */
    @Override
    public boolean restoreLatestState() {
        ApplicationState latestState = caretaker.getLatestMemento();
        
        if (latestState != null) {
            originator.restoreFromMemento(latestState);
            stateChanged = false;
            return true;
        }
        
        return false;
    }
    
    /**
     * Saves the application state to disk.
     * 
     * @return true if successful, false otherwise
     */
    @Override
    public boolean saveStateToDisk() {
        return caretaker.saveHistoryToFile(persistentStateFilename);
    }
    
    /**
     * Loads the application state from disk.
     * 
     * @return true if successful, false otherwise
     */
    @Override
    public boolean loadStateFromDisk() {
        boolean loaded = caretaker.loadHistoryFromFile(persistentStateFilename);
        
        if (loaded && caretaker.getHistorySize() > 0) {
            // Restore the latest state
            restoreLatestState();
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets a list of all saved state snapshots.
     * 
     * @return A list of all state snapshots, newest first
     */
    @Override
    public List<ApplicationState> getStateHistory() {
        return caretaker.getAllMementos();
    }
    
    /**
     * Gets the number of saved state snapshots.
     * 
     * @return The number of snapshots
     */
    @Override
    public int getStateHistorySize() {
        return caretaker.getHistorySize();
    }
    
    /**
     * Clears all saved state snapshots.
     */
    @Override
    public void clearStateHistory() {
        caretaker.clearHistory();
    }
    
    /**
     * Sets multiple state values at once.
     * 
     * @param states A map of key-value pairs to set
     */
    @Override
    public void setMultipleStates(Map<String, Object> states) {
        if (states != null) {
            for (Map.Entry<String, Object> entry : states.entrySet()) {
                originator.setState(entry.getKey(), entry.getValue());
            }
            stateChanged = true;
        }
    }
    
    /**
     * Creates a checkpoint of the current state with a description.
     * 
     * @param description The description for this checkpoint
     */
    @Override
    public void createCheckpoint(String description) {
        originator.setState("checkpointDescription", description);
        originator.setState("checkpointTimestamp", new java.util.Date());
        saveState();
    }
}