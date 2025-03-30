package src.com.es2.designpatterns.StateStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Responsible for keeping track of multiple mementos and providing
 * access to them without exposing their internal details.
 * This is the "Caretaker" in the Memento pattern.
 */
public class StateCaretaker implements IStateCaretaker {
    // Stack of saved states (most recent first)
    private final Stack<ApplicationState> stateHistory;
    
    // Maximum number of states to keep in memory
    private final int maxHistorySize;
    
    // Directory to persist states
    private final String stateDirectory;
    
    /**
     * Creates a new state caretaker with default settings.
     */
    public StateCaretaker() {
        this(10, "app_states");
    }
    
    /**
     * Creates a new state caretaker with the specified settings.
     * 
     * @param maxHistorySize The maximum number of states to keep in memory
     * @param stateDirectory The directory to persist states
     */
    public StateCaretaker(int maxHistorySize, String stateDirectory) {
        this.stateHistory = new Stack<>();
        this.maxHistorySize = maxHistorySize;
        this.stateDirectory = stateDirectory;
        
        // Create the state directory if it doesn't exist
        File directory = new File(stateDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    /**
     * Adds a memento to the history.
     * 
     * @param state The memento to add
     */
    @Override
    public void addMemento(ApplicationState state) {
        if (state != null) {
            stateHistory.push(state);
            
            // Trim the history if it exceeds the maximum size
            while (stateHistory.size() > maxHistorySize) {
                stateHistory.remove(0);
            }
        }
    }
    
    /**
     * Gets the most recent memento from the history.
     * 
     * @return The most recent memento, or null if the history is empty
     */
    @Override
    public ApplicationState getLatestMemento() {
        if (!stateHistory.isEmpty()) {
            return stateHistory.peek();
        }
        return null;
    }
    
    /**
     * Gets the previous memento from the history.
     * This removes the latest memento from the history.
     * 
     * @return The previous memento, or null if there are no previous mementos
     */
    @Override
    public ApplicationState getPreviousMemento() {
        if (!stateHistory.isEmpty()) {
            stateHistory.pop(); // Remove the latest memento
            
            if (!stateHistory.isEmpty()) {
                return stateHistory.peek(); // Return the new latest memento
            }
        }
        return null;
    }
    
    /**
     * Gets all mementos in the history.
     * 
     * @return A list of all mementos, newest first
     */
    @Override
    public List<ApplicationState> getAllMementos() {
        return new ArrayList<>(stateHistory);
    }
    
    /**
     * Clears the memento history.
     */
    @Override
    public void clearHistory() {
        stateHistory.clear();
    }
    
    /**
     * Saves the current memento history to a file.
     * 
     * @param filename The filename to save to
     * @return true if successful, false otherwise
     */
    @Override
    public boolean saveHistoryToFile(String filename) {
        File file = new File(stateDirectory, filename);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(stateHistory);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving state history: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads memento history from a file.
     * 
     * @param filename The filename to load from
     * @return true if successful, false otherwise
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean loadHistoryFromFile(String filename) {
        File file = new File(stateDirectory, filename);
        
        if (!file.exists()) {
            return false;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Stack<ApplicationState> loadedHistory = (Stack<ApplicationState>) ois.readObject();
            stateHistory.clear();
            stateHistory.addAll(loadedHistory);
            return true;
        } catch (Exception e) {
            System.err.println("Error loading state history: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the number of mementos in the history.
     * 
     * @return The number of mementos
     */
    @Override
    public int getHistorySize() {
        return stateHistory.size();
    }
}