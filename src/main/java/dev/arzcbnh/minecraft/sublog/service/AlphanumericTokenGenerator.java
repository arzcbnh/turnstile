package dev.arzcbnh.minecraft.sublog.service;

import java.security.SecureRandom;

public final class AlphanumericTokenGenerator implements TokenGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final SecureRandom random;
    private final int length;

    public AlphanumericTokenGenerator(int length) {
        this(new SecureRandom(), length);
    }

    AlphanumericTokenGenerator(SecureRandom random, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Token length must be positive");
        }

        this.random = random;
        this.length = length;
    }

    @Override
    public String generate() {
        final var builder = new StringBuilder(this.length);
        for (int i = 0; i < this.length; i++) {
            builder.append(CHARACTERS.charAt(this.random.nextInt(CHARACTERS.length())));
        }
        return builder.toString();
    }
}
