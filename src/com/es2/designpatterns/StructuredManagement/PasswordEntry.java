package src.com.es2.designpatterns.StructuredManagement;

import src.com.es2.designpatterns.Credential.Credential;

/**
 * Represents an individual password entry in the password manager.
 * This is a "leaf" node in the Composite pattern.
 */
public class PasswordEntry extends PasswordItem {
    private Credential credential;

    public PasswordEntry(String id, String name, Credential credential) {
        super(id, name);
        this.credential = credential;
    }

    /**
     * Gets the credential associated with this password entry.
     * @return The credential object
     */
    public Credential getCredential() {
        return credential;
    }

    /**
     * Updates the credential associated with this password entry.
     * @param credential The new credential
     */
    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    /**
     * Displays this password entry with the given indentation.
     * @param indent The indentation to use for display
     */
    @Override
    public void display(String indent) {
        System.out.println(indent + "🔑 " + getName() + " (Password: " + credential.getId() + ")");
    }

    /**
     * Searches this password entry for the given query.
     * @param query The search query
     * @return true if the query matches the name or credential, false otherwise
     */
    @Override
    public boolean search(String query) {
        String lowerQuery = query.toLowerCase();
        // Search in the name of the password entry
        if (getName().toLowerCase().contains(lowerQuery)) {
            return true;
        }
        // Search in the credential name or metadata
        if (credential.getName().toLowerCase().contains(lowerQuery)) {
            return true;
        }
        // We could also search in metadata if needed
        return false;
    }
    
    /**
     * Counts the number of passwords in this entry.
     * As this is an individual password, always returns 1.
     * @return Always 1 for a password entry
     */
    @Override
    public int countPasswords() {
        return 1;
    }
}