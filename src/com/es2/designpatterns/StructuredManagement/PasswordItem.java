package src.com.es2.designpatterns.StructuredManagement;

/**
 * Abstract base component for the Composite pattern.
 * Represents any item in the password hierarchy - could be either
 * a password entry or a category.
 */
public abstract class PasswordItem {
    private String id;
    private String name;
    private PasswordCategory parent;

    public PasswordItem(String id, String name) {
        this.id = id;
        this.name = name;
        this.parent = null; // No parent initially
    }

    /**
     * Gets the unique identifier for this password item.
     * @return The ID of this item
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the display name for this password item.
     * @return The name of this item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this password item.
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the parent category of this item.
     * @return The parent category, or null if this is a root item
     */
    public PasswordCategory getParent() {
        return parent;
    }

    /**
     * Sets the parent category of this item.
     * @param parent The new parent category
     */
    public void setParent(PasswordCategory parent) {
        this.parent = parent;
    }

    /**
     * Gets the full path of this item in the hierarchy.
     * @return A string representing the path (e.g., "Personal->Email->Gmail")
     */
    public String getPath() {
        if (parent == null) {
            return name;
        } else {
            return parent.getPath() + "->" + name;
        }
    }

    /**
     * Display method that all password items must implement.
     * This creates a uniform interface for both passwords and categories.
     */
    public abstract void display(String indent);

    /**
     * Method to search within this item for a given query.
     * @param query The search query
     * @return true if this item matches the query, false otherwise
     */
    public abstract boolean search(String query);
    
    /**
     * Method to count the number of passwords within this item.
     * @return The number of passwords
     */
    public abstract int countPasswords();
}