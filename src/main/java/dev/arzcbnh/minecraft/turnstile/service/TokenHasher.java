package dev.arzcbnh.minecraft.turnstile.service;

public interface TokenHasher {
    String hash(String token);

    boolean matches(String token, String encodedHash);
}
