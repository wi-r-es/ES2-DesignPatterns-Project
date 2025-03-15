package src.com.es2.designpatterns.Credential.Generator;

public interface GenerationAlgorithm {
    String generate(SecurityCriteria criteria);
}