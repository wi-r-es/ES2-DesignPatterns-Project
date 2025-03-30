package src.com.es2.designpatterns;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Credential.CredentialFactory;
import src.com.es2.designpatterns.Credential.CredentialType;
import src.com.es2.designpatterns.StateStorage.ApplicationState;
import src.com.es2.designpatterns.StateStorage.IStateManager;
import src.com.es2.designpatterns.StateStorage.RecentCredentialsTracker;
import src.com.es2.designpatterns.StateStorage.StateManager;

import java.util.Date;
import java.util.List;

public class StateStorageTest {
    public static void main(String[] args) {
        System.out.println("===== Testing State Storage Pattern =====\n");
        
        // Get the state manager
        IStateManager stateManager = StateManager.getInstance();
        
        // Test basic state operations
        testBasicStateOperations(stateManager);
        
        // Test state history and restoration
        testStateHistoryAndRestoration(stateManager);
        
        // Test recent credentials tracking
        testRecentCredentialsTracking();
        
        // Test persistent state
        testPersistentState(stateManager);
    }
    
    /**
     * Tests basic state operations.
     */
    protected static void testBasicStateOperations(IStateManager stateManager) {
        System.out.println("\n--- Testing Basic State Operations ---");
        
        // Set some state values
        stateManager.setState("lastLoginTime", new Date());
        stateManager.setState("username", "johndoe");
        stateManager.setState("theme", "dark");
        
        // Create a checkpoint
        stateManager.createCheckpoint("Initial state");
        
        // Retrieve and display state values
        Date lastLogin = stateManager.getState("lastLoginTime");
        String username = stateManager.getState("username");
        String theme = stateManager.getState("theme");
        
        System.out.println("Last login: " + lastLogin);
        System.out.println("Username: " + username);
        System.out.println("Theme: " + theme);
        
        // Modify a state value
        System.out.println("\nChanging theme to light...");
        stateManager.setState("theme", "light");
        theme = stateManager.getState("theme");
        System.out.println("Theme: " + theme);
        
        // Save the state
        stateManager.saveState();
        
        // Remove a state value
        System.out.println("\nRemoving username...");
        stateManager.removeState("username");
        username = stateManager.getState("username");
        System.out.println("Username: " + (username != null ? username : "null"));
        
        // Save the state again
        stateManager.saveState();
        
        System.out.println("\nBasic state operations completed.");
    }
    
    /**
     * Tests state history and restoration.
     */
    protected static void testStateHistoryAndRestoration(IStateManager stateManager) {
        System.out.println("\n--- Testing State History and Restoration ---");
        
        // Display current state history
        List<ApplicationState> history = stateManager.getStateHistory();
        System.out.println("State history size: " + history.size());
        
        for (int i = 0; i < history.size(); i++) {
            ApplicationState state = history.get(i);
            System.out.println("State " + (i + 1) + ": " + state.getTimestamp());
        }
        
        // Restore to previous state
        System.out.println("\nRestoring to previous state...");
        if (stateManager.restorePreviousState()) {
            // Check the restored values
            String theme = stateManager.getState("theme");
            String username = stateManager.getState("username");
            
            System.out.println("Restored theme: " + theme);
            System.out.println("Restored username: " + username);
        } else {
            System.out.println("No previous state to restore.");
        }
        
        // Set some new values and create another checkpoint
        System.out.println("\nSetting new values...");
        stateManager.setState("language", "en");
        stateManager.setState("notifications", true);
        
        // Create a checkpoint
        stateManager.createCheckpoint("Updated settings");
        
        // Verify the new values
        String language = stateManager.getState("language");
        Boolean notifications = stateManager.getState("notifications");
        
        System.out.println("Language: " + language);
        System.out.println("Notifications: " + notifications);
        
        System.out.println("\nState history and restoration test completed.");
    }
    
    /**
     * Tests the recent credentials tracking functionality.
     */
    protected static void testRecentCredentialsTracking() {
        System.out.println("\n--- Testing Recent Credentials Tracking ---");
        
        // Get the tracker
        RecentCredentialsTracker tracker = RecentCredentialsTracker.getInstance();
        
        // Create some sample credentials
        CredentialFactory factory = CredentialFactory.getInstance();
        
        Credential cred1 = factory.createCredential(CredentialType.PASSWORD);
        Credential cred2 = factory.createCredential(CredentialType.PASSWORD);
        Credential cred3 = factory.createCredential(CredentialType.PASSWORD);
        
        // Track credential access
        System.out.println("Tracking access to credential: " + cred1.getId());
        tracker.trackCredentialAccess(cred1);
        
        try {
            Thread.sleep(100); // Ensure different timestamps
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Tracking access to credential: " + cred2.getId());
        tracker.trackCredentialAccess(cred2);
        
        try {
            Thread.sleep(100); // Ensure different timestamps
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Tracking access to credential: " + cred3.getId());
        tracker.trackCredentialAccess(cred3);
        
        // Get recent credentials
        List<String> recentIds = tracker.getRecentCredentialIds();
        
        System.out.println("\nRecent credentials (most recent first):");
        for (String id : recentIds) {
            Date accessTime = tracker.getAccessTimestamp(id);
            System.out.println("Credential: " + id + ", Accessed: " + accessTime);
        }
        
        // Access first credential again (should move to the top)
        System.out.println("\nAccessing credential again: " + cred1.getId());
        tracker.trackCredentialAccess(cred1);
        
        // Get updated recent credentials
        recentIds = tracker.getRecentCredentialIds();
        
        System.out.println("\nUpdated recent credentials (most recent first):");
        for (String id : recentIds) {
            Date accessTime = tracker.getAccessTimestamp(id);
            System.out.println("Credential: " + id + ", Accessed: " + accessTime);
        }
        
        System.out.println("\nRecent credentials tracking test completed.");
    }
    
    /**
     * Tests the persistent state functionality.
     */
    protected static void testPersistentState(IStateManager stateManager) {
        System.out.println("\n--- Testing Persistent State ---");
        
        // Save current state to disk
        System.out.println("Saving state to disk...");
        boolean savedSuccessfully = stateManager.saveStateToDisk();
        System.out.println("State saved successfully: " + savedSuccessfully);
        
        // Create a simulated restart
        System.out.println("\nSimulating application restart...");
        
        // Clear the in-memory state (we wouldn't normally do this,
        // but it simulates a restart for testing purposes)
        stateManager.clearStateHistory();
        
        // Load state from disk
        System.out.println("Loading state from disk...");
        boolean loadedSuccessfully = stateManager.loadStateFromDisk();
        System.out.println("State loaded successfully: " + loadedSuccessfully);
        
        if (loadedSuccessfully) {
            // Verify some state values
            String theme = stateManager.getState("theme");
            Boolean notifications = stateManager.getState("notifications");
            String language = stateManager.getState("language");
            
            System.out.println("\nVerifying restored state:");
            System.out.println("Theme: " + theme);
            System.out.println("Notifications: " + notifications);
            System.out.println("Language: " + language);
        }
        
        System.out.println("\nPersistent state test completed.");
    }
}