package src.com.es2.designpatterns.StateStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * A mock implementation of IStateOriginator for testing purposes.
 */
public class MockStateOriginator implements IStateOriginator {
    private Map<String, Object> state = new HashMap<>();
    private boolean saveToMementoWasCalled = false;
    private boolean restoreFromMementoWasCalled = false;
    private boolean setStateCalled = false;
    
    @Override
    public void setState(String key, Object value) {
        setStateCalled = true;
        state.put(key, value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getState(String key) {
        return (T) state.get(key);
    }
    
    @Override
    public void removeState(String key) {
        state.remove(key);
    }
    
    @Override
    public void clearState() {
        state.clear();
    }
    
    @Override
    public ApplicationState saveToMemento() {
        saveToMementoWasCalled = true;
        return new ApplicationState(state);
    }
    
    @Override
    public void restoreFromMemento(ApplicationState memento) {
        restoreFromMementoWasCalled = true;
        if (memento != null) {
            state = memento.getState();
        }
    }
    
    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>(state);
    }
    
    public boolean wasSaveToMementoCalled() {
        return saveToMementoWasCalled;
    }
    
    public boolean wasRestoreFromMementoCalled() {
        return restoreFromMementoWasCalled;
    }
    
    public boolean wasSetStateCalled() {
        return setStateCalled;
    }
    
    public void reset() {
        saveToMementoWasCalled = false;
        restoreFromMementoWasCalled = false;
        setStateCalled = false;
    }
}

