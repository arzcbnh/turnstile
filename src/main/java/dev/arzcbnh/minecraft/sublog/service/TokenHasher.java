package dev.arzcbnh.minecraft.sublog.service;

public interface TokenHasher {
    String hash(String token);

    boolean matches(String token, String encodedHash);
}
