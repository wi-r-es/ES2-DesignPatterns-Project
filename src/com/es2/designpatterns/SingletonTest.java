package src.com.es2.designpatterns;

import src.com.es2.designpatterns.Configuration.ConfigurationManager;
import src.com.es2.designpatterns.Credential.CredentialFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingletonTest {
    public static void main(String[] args) throws InterruptedException {
        // Thread pool to test Singleton instance
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        System.out.println("===== Testing Singleton Pattern Configuration Manager =====");
        // Submit tasks to print Singleton instances
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                ConfigurationManager singletonTest = ConfigurationManager.getInstance();
                singletonTest.showMessage();
            });
        }

        Thread.sleep(1000);
        System.out.println("===== Testing Singleton Pattern CredentialFactory =====");
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                CredentialFactory singletonTest2 = CredentialFactory.getInstance();
                singletonTest2.showMessage();
            });
        }

        // Shutdown the thread pool
        executorService.shutdown();

    }
}
