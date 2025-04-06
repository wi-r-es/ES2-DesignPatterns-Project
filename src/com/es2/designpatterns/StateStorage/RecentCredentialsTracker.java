package src.com.es2.designpatterns.StateStorage;

import src.com.es2.designpatterns.Credential.Credential;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Tracks recently accessed credentials. This class uses the StateManager
 * to persist its state between application sessions.
 */
public class RecentCredentialsTracker implements Serializable {
    private static final long serialVersionUID = 1L;

    // Singleton instance
    private static RecentCredentialsTracker instance;

    // Maximum number of recent credentials to track
    private final int maxRecentCredentials;

    // Map of credential IDs to access timestamps (most recent first)
    private Map<String, Date> recentCredentials;

    // Reference to the state manager (transient to avoid serialization issues)
    private transient IStateManager stateManager;

    // Key for storing in the state manager
    private static final String STATE_KEY = "recentCredentials";

    /**
     * Private constructor for singleton pattern.
     */
    private RecentCredentialsTracker() {
        this.maxRecentCredentials = 20;

        // Inicializa a estrutura de dados sem chamar imediatamente o StateManager
        this.recentCredentials = new LinkedHashMap<String, Date>(maxRecentCredentials, 0.75f, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Date> eldest) {
                return size() > maxRecentCredentials;
            }
        };
    }

    /**
     * Obtém a instância única do RecentCredentialsTracker (singleton).
     */
    public static synchronized RecentCredentialsTracker getInstance() {
        if (instance == null) {
            instance = new RecentCredentialsTracker();
        }
        return instance;
    }

    /**
     * Obtém a instância única do RecentCredentialsTracker com um stateManager personalizado (para testes).
     */
    public static synchronized RecentCredentialsTracker getInstance(IStateManager stateManager) {
        if (instance == null) {
            instance = new RecentCredentialsTracker();
        }
        instance.setStateManager(stateManager);
        return instance;
    }

    /**
     * Adiciona um credencial ao histórico de acessos.
     */
    public void trackCredentialAccess(Credential credential) {
        if (credential != null) {
            recentCredentials.put(credential.getId(), new Date());
            saveState();
        }
    }

    /**
     * Obtém os IDs das credenciais acessadas recentemente, ordenadas do mais recente para o mais antigo.
     */
    public List<String> getRecentCredentialIds() {
        List<String> ids = new ArrayList<>(recentCredentials.keySet());
        ids.sort((id1, id2) -> recentCredentials.get(id2).compareTo(recentCredentials.get(id1)));
        return ids;
    }

    /**
     * Obtém a data de acesso de uma credencial específica.
     */
    public Date getAccessTimestamp(String credentialId) {
        return recentCredentials.get(credentialId);
    }

    /**
     * Limpa o histórico de credenciais recentes.
     */
    public void clearHistory() {
        recentCredentials.clear();
        saveState();
    }

    /**
     * Remove uma credencial do histórico de acessos.
     */
    public void removeCredential(String credentialId) {
        recentCredentials.remove(credentialId);
        saveState();
    }

    /**
     * Obtém a quantidade de credenciais no histórico.
     */
    public int getRecentCredentialsCount() {
        return recentCredentials.size();
    }

    /**
     * Método privado para acessar o `StateManager` apenas quando necessário.
     */
    private IStateManager getStateManager() {
        if (stateManager == null) {
            stateManager = StateManager.getInstance();
        }
        return stateManager;
    }

    /**
     * Define o `StateManager` explicitamente (usado para testes).
     */
    public void setStateManager(IStateManager stateManager) {
        this.stateManager = stateManager;
    }

    /**
     * Salva o estado atual no `StateManager`.
     */
    private void saveState() {
        getStateManager().setState(STATE_KEY, recentCredentials);
        getStateManager().saveState();
    }

    /**
     * Serialização personalizada: grava os objetos normalmente.
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * Desserialização personalizada: evita chamar `StateManager.getInstance()` diretamente.
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.stateManager = null; // Evita recursão infinita ao restaurar o objeto
    }
}