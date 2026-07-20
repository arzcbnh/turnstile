package dev.arzcbnh.minecraft.turnstile.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class Sha256TokenHasher implements TokenHasher {
    @Override
    public String hash(String token) {
        return Base64.getEncoder().encodeToString(this.digest(token));
    }

    @Override
    public boolean matches(String token, String encodedHash) {
        try {
            return MessageDigest.isEqual(this.digest(token), Base64.getDecoder().decode(encodedHash));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private byte[] digest(String token) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
