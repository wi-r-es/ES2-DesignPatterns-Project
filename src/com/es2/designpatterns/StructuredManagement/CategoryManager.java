package src.com.es2.designpatterns.StructuredManagement;

import src.com.es2.designpatterns.Credential.Credential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the overall category structure of the password manager.
 * This class serves as a facade to the Composite pattern implementation.
 */
public class CategoryManager {
    // Singleton instance
    private static CategoryManager instance;
    
    // Root categories
    private List<PasswordCategory> rootCategories;
    
    // Index for quick lookups by ID
    private Map<String, PasswordItem> itemIndex;
    
    /**
     * Private constructor for singleton pattern.
     */
    private CategoryManager() {
        rootCategories = new ArrayList<>();
        itemIndex = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance of the CategoryManager.
     * @return The singleton instance
     */
    public static synchronized CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }
    
    /**
     * Creates a new root category.
     * @param name The name of the category
     * @return The newly created category
     */
    public PasswordCategory createRootCategory(String name) {
        String id = UUID.randomUUID().toString();
        PasswordCategory category = new PasswordCategory(id, name);
        rootCategories.add(category);
        itemIndex.put(id, category);
        return category;
    }
    
    /**
     * Creates a subcategory within an existing category.
     * @param parentId The ID of the parent category
     * @param name The name of the new subcategory
     * @return The newly created subcategory, or null if the parent wasn't found
     */
    public PasswordCategory createSubcategory(String parentId, String name) {
        PasswordItem parent = itemIndex.get(parentId);
        
        if (parent instanceof PasswordCategory) {
            PasswordCategory parentCategory = (PasswordCategory) parent;
            PasswordCategory subcategory = parentCategory.createSubcategory(name);
            itemIndex.put(subcategory.getId(), subcategory);
            return subcategory;
        }
        
        return null; // Parent not found or not a category
    }
    
    /**
     * Creates a password entry within a category.
     * @param categoryId The ID of the category
     * @param name The name of the password entry
     * @param credential The credential to associate with the entry
     * @return The newly created password entry, or null if the category wasn't found
     */
    public PasswordEntry createPasswordEntry(String categoryId, String name, Credential credential) {
        PasswordItem category = itemIndex.get(categoryId);
        
        if (category instanceof PasswordCategory) {
            String id = UUID.randomUUID().toString();
            PasswordEntry entry = new PasswordEntry(id, name, credential);
            ((PasswordCategory) category).addItem(entry);
            itemIndex.put(id, entry);
            return entry;
        }
        
        return null; // Category not found
    }
    
    /**
     * Gets an item by its ID.
     * @param id The ID of the item to get
     * @return The item, or null if not found
     */
    public PasswordItem getItem(String id) {
        return itemIndex.get(id);
    }
    
    /**
     * Gets all root categories.
     * @return A list of all root categories
     */
    public List<PasswordCategory> getRootCategories() {
        return new ArrayList<>(rootCategories);
    }
    
    /**
     * Removes an item by its ID.
     * @param id The ID of the item to remove
     * @return true if the item was removed, false if it wasn't found
     */
    public boolean removeItem(String id) {
        PasswordItem item = itemIndex.get(id);
        
        if (item == null) {
            return false;
        }
        
        // Remove from index
        itemIndex.remove(id);
        
        // If it's a root category, remove from root categories list
        if (item.getParent() == null) {
            return rootCategories.remove(item);
        }
        
        // Otherwise, remove from its parent
        return item.getParent().removeItem(id);
    }
    
    /**
     * Searches all items for the given query.
     * @param query The search query
     * @return A list of items that match the query
     */
    public List<PasswordItem> search(String query) {
        List<PasswordItem> results = new ArrayList<>();
        
        for (PasswordItem item : itemIndex.values()) {
            if (item.search(query)) {
                results.add(item);
            }
        }
        
        return results;
    }
    
    /**
     * Displays the entire category structure.
     */
    public void displayAll() {
        System.out.println("Password Manager Categories:");
        for (PasswordCategory category : rootCategories) {
            category.display("  ");
        }
    }
    
    /**
     * Creates a category path (e.g., "Personal->Email->Gmail") and returns the last category.
     * If any part of the path doesn't exist, it will be created.
     * @param path The path to create, with category names separated by "->"
     * @return The last category in the path
     */
    public PasswordCategory createCategoryPath(String path) {
        String[] parts = path.split("->");
        
        if (parts.length == 0) {
            return null;
        }
        
        // Find or create the root category
        PasswordCategory current = null;
        for (PasswordCategory root : rootCategories) {
            if (root.getName().equals(parts[0])) {
                current = root;
                break;
            }
        }
        
        if (current == null) {
            current = createRootCategory(parts[0]);
        }
        
        // Create the rest of the path
        for (int i = 1; i < parts.length; i++) {
            String categoryName = parts[i];
            
            // Look for an existing subcategory with this name
            boolean found = false;
            for (PasswordItem item : current.getItems()) {
                if (item instanceof PasswordCategory && item.getName().equals(categoryName)) {
                    current = (PasswordCategory) item;
                    found = true;
                    break;
                }
            }
            
            // If not found, create a new subcategory
            if (!found) {
                current = current.createSubcategory(categoryName);
                itemIndex.put(current.getId(), current);
            }
        }
        
        return current;
    }
}