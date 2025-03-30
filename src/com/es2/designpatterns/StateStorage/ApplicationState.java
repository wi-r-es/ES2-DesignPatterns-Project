package src.com.es2.designpatterns.StateStorage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

/**
 * Represents a snapshot of the application state at a particular moment.
 * This is the "Memento" in the Memento pattern.
 */
public class ApplicationState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // The captured state data
    private final Map<String, Serializable> state;
    
    // When this state was captured
    private final Date timestamp;
    
    /**
     * Creates a new application state snapshot.
     * 
     * @param state The state to capture
     */
    // public ApplicationState(Map<String, Object> state) {
    //     // Make a deep copy of the state to ensure immutability
    //     this.state = new HashMap<>();
    //     if (state != null) {
    //         this.state.putAll(state);
    //     }
    //     this.timestamp = new Date();
    // }
    public ApplicationState(Map<String, Object> state) {
        this.state = new HashMap<>();
        if (state != null) {
            for (Map.Entry<String, Object> entry : state.entrySet()) {
                if (entry.getValue() instanceof Serializable) {
                    this.state.put(entry.getKey(), (Serializable)entry.getValue());
                } else {
                    // Log or handle non-serializable objects
                    System.out.println("Warning: Non-serializable object skipped: " + entry.getKey());
                }
            }
        }
        this.timestamp = new Date();
    }
    
    /**
     * Gets the captured state data.
     * 
     * @return A copy of the state data
     */
    public Map<String, Object> getState() {
        // Return a copy to maintain immutability
        return new HashMap<>(state);
    }
    
    /**
     * Gets the timestamp when this state was captured.
     * 
     * @return The timestamp
     */
    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }
    
    /**
     * Gets a specific state value by key.
     * 
     * @param key The key for the state value
     * @return The state value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) state.get(key);
    }
    
    /**
     * Gets a description of this state snapshot.
     * 
     * @return A descriptive string
     */
    public String getDescription() {
        StringBuilder description = new StringBuilder();
        description.append("Application State - ").append(timestamp).append("\n");
        
        // Add details about the state
        for (Map.Entry<String, Serializable> entry : state.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value != null) {
                if (key.contains("Password") || key.contains("Credential")) {
                    // Don't include actual sensitive values in the description
                    description.append("  - ").append(key).append(": [Protected]\n");
                } else {
                    description.append("  - ").append(key).append(": ")
                              .append(value.toString()).append("\n");
                }
            }
        }
        
        return description.toString();
    }
    
    @Override
    public String toString() {
        return "ApplicationState[timestamp=" + timestamp + ", stateSize=" + state.size() + "]";
    }
}