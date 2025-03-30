package src.com.es2.designpatterns.StateStorage;

import java.util.List;

/**
 * Interface for the state caretaker component.
 * Defines operations for managing mementos.
 */
public interface IStateCaretaker {
    /**
     * Adds a memento to the history.
     * 
     * @param state The memento to add
     */
    void addMemento(ApplicationState state);
    
    /**
     * Gets the most recent memento from the history.
     * 
     * @return The most recent memento, or null if the history is empty
     */
    ApplicationState getLatestMemento();
    
    /**
     * Gets the previous memento from the history.
     * This removes the latest memento from the history.
     * 
     * @return The previous memento, or null if there are no previous mementos
     */
    ApplicationState getPreviousMemento();
    
    /**
     * Gets all mementos in the history.
     * 
     * @return A list of all mementos, newest first
     */
    List<ApplicationState> getAllMementos();
    
    /**
     * Clears the memento history.
     */
    void clearHistory();
    
    /**
     * Saves the current memento history to a file.
     * 
     * @param filename The filename to save to
     * @return true if successful, false otherwise
     */
    boolean saveHistoryToFile(String filename);
    
    /**
     * Loads memento history from a file.
     * 
     * @param filename The filename to load from
     * @return true if successful, false otherwise
     */
    boolean loadHistoryFromFile(String filename);
    
    /**
     * Gets the number of mementos in the history.
     * 
     * @return The number of mementos
     */
    int getHistorySize();
}