package src.com.es2.designpatterns.Credential.Generator;

import src.com.es2.designpatterns.Credential.SecurityCriteria;

public interface GenerationAlgorithm {
    String generate(SecurityCriteria criteria);
}