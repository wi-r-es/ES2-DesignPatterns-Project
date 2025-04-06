package src.com.es2.designpatterns.FunctionalityExtender;

import src.com.es2.designpatterns.Storage.StorageFactory;
import src.com.es2.designpatterns.Storage.StorageType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class LoggingExtension extends PasswordAccessDecorator {
    private static final String LOG_FILE = "access.log";

    public LoggingExtension(PasswordAccessHandler wrapped) {
        super(wrapped);
    }

    @Override
    public void access(String credentialId, StorageFactory storageFactory) {
        logToFile("[" + LocalDateTime.now() + "] Acesso solicitado para: " + credentialId);
        super.access(credentialId, storageFactory);
    }

    @Override
    public void access(String credentialId, StorageFactory storageFactory, StorageType type) {
        logToFile("[" + LocalDateTime.now() + "] Acesso solicitado para: " + credentialId);
        super.access(credentialId, storageFactory, type);
    }

    private synchronized void logToFile(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
