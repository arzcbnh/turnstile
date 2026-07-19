package dev.arzcbnh.minecraft.sublog.token;

import java.security.SecureRandom;

// TODO: make providers abstract, maybe configurable
public class AlphanumericTokenProvider {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final SecureRandom random = new SecureRandom();

    public String generate(int length) {
        final var builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            builder.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return builder.toString();
    }
}
