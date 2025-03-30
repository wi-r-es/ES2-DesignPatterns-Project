package src.com.es2.designpatterns;

import src.com.es2.designpatterns.Credential.Credential;
import src.com.es2.designpatterns.Credential.CredentialFactory;
import src.com.es2.designpatterns.Credential.CredentialType;
import src.com.es2.designpatterns.Credential.SecurityCriteria;
import src.com.es2.designpatterns.StructuredManagement.CategoryManager;
import src.com.es2.designpatterns.StructuredManagement.PasswordCategory;
import src.com.es2.designpatterns.StructuredManagement.PasswordEntry;
import src.com.es2.designpatterns.StructuredManagement.PasswordItem;

import java.util.List;

public class CategoryMgmtTest {
    public static void main(String[] args) {
        // Get the category manager singleton
        CategoryManager manager = CategoryManager.getInstance();
        
        System.out.println("===== Testing Password Category Management =====\n");
        
        // Create sample credentials using the CredentialFactory
        CredentialFactory credentialFactory = CredentialFactory.getInstance();
        
        Credential trelloCredential = credentialFactory.createCredential(CredentialType.PASSWORD);
        Credential phoneCredential = credentialFactory.createCredential(CredentialType.PASSWORD);
        
        // Create the hierarchy from the example:
        // Pessoal->Produtividade->Trello->*(Password)*
        // Pessoal->Telefone->*(Password)*
        System.out.println("Creating category hierarchy...");
        
        // Method 1: Create categories step by step
        PasswordCategory pessoal = manager.createRootCategory("Pessoal");
        PasswordCategory produtividade = manager.createSubcategory(pessoal.getId(), "Produtividade");
        PasswordCategory trello = manager.createSubcategory(produtividade.getId(), "Trello");
        
        // Add a password to Trello category
        PasswordEntry trelloEntry = manager.createPasswordEntry(
            trello.getId(), "Conta Trello", trelloCredential);
        
        // Method 2: Create categories using a path
        PasswordCategory telefone = manager.createCategoryPath("Pessoal->Telefone");
        
        // Add a password to Telefone category
        PasswordEntry phoneEntry = manager.createPasswordEntry(
            telefone.getId(), "PIN Telefone", phoneCredential);
        
        // Display the entire structure
        System.out.println("\nCategory Structure:");
        manager.displayAll();
        
        // Demonstrate getting the full path of an item
        System.out.println("\nFull paths of password entries:");
        System.out.println("- " + trelloEntry.getPath());
        System.out.println("- " + phoneEntry.getPath());
        
        // Demonstrate counting passwords
        System.out.println("\nCounting passwords:");
        System.out.println("Passwords in 'Pessoal': " + pessoal.countPasswords());
        System.out.println("Passwords in 'Produtividade': " + produtividade.countPasswords());
        System.out.println("Passwords in 'Trello': " + trello.countPasswords());
        System.out.println("Passwords in 'Telefone': " + telefone.countPasswords());
        
        // Demonstrate searching
        System.out.println("\nSearching for 'tel':");
        List<PasswordItem> searchResults = manager.search("tel");
        for (PasswordItem item : searchResults) {
            System.out.println("- " + item.getName() + " (" + item.getPath() + ")");
        }
        
        // Demonstrate removing items
        System.out.println("\nRemoving 'Trello' category...");
        manager.removeItem(trello.getId());
        manager.displayAll();
        
        // Create another password entry directly in 'Pessoal'
        System.out.println("\nAdding a password directly to 'Pessoal'...");
        Credential emailCredential = credentialFactory.createCredential(CredentialType.PASSWORD);
        PasswordEntry emailEntry = manager.createPasswordEntry(
            pessoal.getId(), "Email Pessoal", emailCredential);
        manager.displayAll();
    }
}