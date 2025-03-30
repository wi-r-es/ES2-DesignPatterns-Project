package src.com.es2.designpatterns.StructuredManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Represents a category of passwords that can contain other categories or
 * individual password entries. This is a "composite" node in the Composite pattern.
 */
public class PasswordCategory extends PasswordItem {
    private List<PasswordItem> children;

    public PasswordCategory(String id, String name) {
        super(id, name);
        this.children = new ArrayList<>();
    }

    /**
     * Adds a password item (either a category or a password entry) to this category.
     * @param item The item to add
     */
    public void addItem(PasswordItem item) {
        // Set this category as the parent of the item
        item.setParent(this);
        
        // Add the item to this category's children
        children.add(item);
    }

    /**
     * Removes an item from this category.
     * @param item The item to remove
     * @return true if the item was removed, false if it wasn't found
     */
    public boolean removeItem(PasswordItem item) {
        if (children.remove(item)) {
            // Remove the parent reference from the item
            item.setParent(null);
            return true;
        }
        return false;
    }

    /**
     * Removes an item from this category by ID.
     * @param id The ID of the item to remove
     * @return true if the item was removed, false if it wasn't found
     */
    public boolean removeItem(String id) {
        Iterator<PasswordItem> iterator = children.iterator();
        while (iterator.hasNext()) {
            PasswordItem item = iterator.next();
            if (item.getId().equals(id)) {
                iterator.remove();
                item.setParent(null);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an item from this category by ID.
     * @param id The ID of the item to get
     * @return The item, or null if not found
     */
    public PasswordItem getItem(String id) {
        // First, check directly in this category
        for (PasswordItem item : children) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        
        // If not found, recursively check in subcategories
        for (PasswordItem item : children) {
            if (item instanceof PasswordCategory) {
                PasswordCategory subcategory = (PasswordCategory) item;
                PasswordItem found = subcategory.getItem(id);
                if (found != null) {
                    return found;
                }
            }
        }
        
        // Not found anywhere
        return null;
    }

    /**
     * Gets all items in this category (not including items in subcategories).
     * @return A list of items in this category
     */
    public List<PasswordItem> getItems() {
        return new ArrayList<>(children);
    }

    /**
     * Gets all items in this category and all its subcategories recursively.
     * @return A list of all items in the hierarchy under this category
     */
    public List<PasswordItem> getAllItems() {
        List<PasswordItem> allItems = new ArrayList<>();
        
        // Add direct children
        allItems.addAll(children);
        
        // Recursively add items from subcategories
        for (PasswordItem item : children) {
            if (item instanceof PasswordCategory) {
                PasswordCategory subcategory = (PasswordCategory) item;
                allItems.addAll(subcategory.getAllItems());
            }
        }
        
        return allItems;
    }

    /**
     * Creates a new subcategory in this category.
     * @param name The name of the new subcategory
     * @return The newly created subcategory
     */
    public PasswordCategory createSubcategory(String name) {
        String id = UUID.randomUUID().toString();
        PasswordCategory subcategory = new PasswordCategory(id, name);
        addItem(subcategory);
        return subcategory;
    }

    /**
     * Displays this category and all its contents with the given indentation.
     * @param indent The indentation to use for display
     */
    @Override
    public void display(String indent) {
        System.out.println(indent + "📁 " + getName() + " (" + children.size() + " items)");
        
        // Display all child items with increased indentation
        String childIndent = indent + "    ";
        for (PasswordItem item : children) {
            item.display(childIndent);
        }
    }

    /**
     * Searches this category and all its contents for the given query.
     * @param query The search query
     * @return true if any item in this category or its subcategories matches the query
     */
    @Override
    public boolean search(String query) {
        String lowerQuery = query.toLowerCase();
        
        // Check if the category name matches
        if (getName().toLowerCase().contains(lowerQuery)) {
            return true;
        }
        
        // Check all children
        for (PasswordItem item : children) {
            if (item.search(query)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Counts the number of passwords in this category and all its subcategories.
     * @return The total number of passwords
     */
    @Override
    public int countPasswords() {
        int count = 0;
        
        // Count passwords in all children
        for (PasswordItem item : children) {
            count += item.countPasswords();
        }
        
        return count;
    }
}