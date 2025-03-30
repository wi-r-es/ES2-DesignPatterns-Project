package src.com.es2.designpatterns.StateStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for creating mementos of its state and restoring from them.
 * This is the "Originator" in the Memento pattern.
 */
public class StateOriginator implements IStateOriginator {
    // The current state of the application
    private Map<String, Object> currentState;
    
    /**
     * Creates a new state originator with an empty state.
     */
    public StateOriginator() {
        this.currentState = new HashMap<>();
    }
    
    /**
     * Sets a value in the current state.
     * 
     * @param key The key for the value
     * @param value The value to store
     */
    @Override
    public void setState(String key, Object value) {
        currentState.put(key, value);
    }
    
    /**
     * Gets a value from the current state.
     * 
     * @param key The key for the value
     * @return The stored value, or null if not found
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getState(String key) {
        return (T) currentState.get(key);
    }
    
    /**
     * Removes a value from the current state.
     * 
     * @param key The key for the value to remove
     */
    @Override
    public void removeState(String key) {
        currentState.remove(key);
    }
    
    /**
     * Clears all values from the current state.
     */
    @Override
    public void clearState() {
        currentState.clear();
    }
    
    /**
     * Creates a memento containing a snapshot of the current state.
     * 
     * @return The created memento
     */
    @Override
    public ApplicationState saveToMemento() {
        return new ApplicationState(currentState);
    }
    
    /**
     * Restores the state from a memento.
     * 
     * @param memento The memento to restore from
     */
    @Override
    public void restoreFromMemento(ApplicationState memento) {
        if (memento != null) {
            currentState = memento.getState();
        }
    }
    
    /**
     * Gets the entire current state.
     * 
     * @return A copy of the current state
     */
    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>(currentState);
    }
}