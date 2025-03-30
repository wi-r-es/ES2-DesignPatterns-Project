package src.com.es2.designpatterns.StateStorage;

import java.util.Map;

/**
 * Interface for the state originator component.
 * Defines operations for creating and restoring from mementos.
 */
public interface IStateOriginator {
    /**
     * Sets a value in the current state.
     * 
     * @param key The key for the value
     * @param value The value to store
     */
    void setState(String key, Object value);
    
    /**
     * Gets a value from the current state.
     * 
     * @param key The key for the value
     * @return The stored value, or null if not found
     */
    <T> T getState(String key);
    
    /**
     * Removes a value from the current state.
     * 
     * @param key The key for the value to remove
     */
    void removeState(String key);
    
    /**
     * Clears all values from the current state.
     */
    void clearState();
    
    /**
     * Creates a memento containing a snapshot of the current state.
     * 
     * @return The created memento
     */
    ApplicationState saveToMemento();
    
    /**
     * Restores the state from a memento.
     * 
     * @param memento The memento to restore from
     */
    void restoreFromMemento(ApplicationState memento);
    
    /**
     * Gets the entire current state.
     * 
     * @return A copy of the current state
     */
    Map<String, Object> getCurrentState();
}