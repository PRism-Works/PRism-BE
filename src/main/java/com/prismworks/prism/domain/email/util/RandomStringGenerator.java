package com.prismworks.prism.domain.email.util;

import java.security.SecureRandom;
import java.util.UUID;

public class RandomStringGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generate(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for(int i = 0; i < length; ++i) {
            int index = SECURE_RANDOM.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(index));
        }

        return stringBuilder.toString();
    }
}
