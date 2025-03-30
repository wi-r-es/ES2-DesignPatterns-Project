package src.com.es2.designpatterns.StateStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock implementation of IStateCaretaker for testing purposes.
 */
public class MockStateCaretaker implements IStateCaretaker {
    private List<ApplicationState> mementos = new ArrayList<>();
    private boolean addMementoWasCalled = false;
    private boolean getLatestMementoWasCalled = false;
    private boolean getPreviousMementoWasCalled = false;
    
    @Override
    public void addMemento(ApplicationState state) {
        addMementoWasCalled = true;
        if (state != null) {
            mementos.add(state);
        }
    }
    
    @Override
    public ApplicationState getLatestMemento() {
        getLatestMementoWasCalled = true;
        if (!mementos.isEmpty()) {
            return mementos.get(mementos.size() - 1);
        }
        return null;
    }
    
    @Override
    public ApplicationState getPreviousMemento() {
        getPreviousMementoWasCalled = true;
        if (mementos.size() > 1) {
            mementos.remove(mementos.size() - 1);
            return mementos.get(mementos.size() - 1);
        }
        return null;
    }
    
    @Override
    public List<ApplicationState> getAllMementos() {
        return new ArrayList<>(mementos);
    }
    
    @Override
    public void clearHistory() {
        mementos.clear();
    }
    
    @Override
    public boolean saveHistoryToFile(String filename) {
        // Mock implementation doesn't actually save to file
        return true;
    }
    
    @Override
    public boolean loadHistoryFromFile(String filename) {
        // Mock implementation doesn't actually load from file
        return false;
    }
    
    @Override
    public int getHistorySize() {
        return mementos.size();
    }
    
    public boolean wasAddMementoCalled() {
        return addMementoWasCalled;
    }
    
    public boolean wasGetLatestMementoCalled() {
        return getLatestMementoWasCalled;
    }
    
    public boolean wasGetPreviousMementoCalled() {
        return getPreviousMementoWasCalled;
    }
    
    public void reset() {
        addMementoWasCalled = false;
        getLatestMementoWasCalled = false;
        getPreviousMementoWasCalled = false;
    }
}