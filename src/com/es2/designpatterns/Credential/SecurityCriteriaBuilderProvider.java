package src.com.es2.designpatterns.Credential;

public interface SecurityCriteriaBuilderProvider {
    SecurityCriteria.Builder getBuilderFor(CredentialType type);
}
