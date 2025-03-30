package src.com.es2.designpatterns.Credential.Generator;

import src.com.es2.designpatterns.Credential.SecurityCriteria;

import java.security.SecureRandom;
import java.util.Random;

public class PinAlgorithm implements GenerationAlgorithm {
    @Override
    public String generate(SecurityCriteria criteria) {
        StringBuilder pin = new StringBuilder();
            Random random = new SecureRandom();
            for (int i = 0; i < criteria.getLength(); i++) {
                pin.append(random.nextInt(6));
            }
        return pin.toString();
    }
}