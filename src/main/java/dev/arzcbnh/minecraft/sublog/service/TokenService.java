package dev.arzcbnh.minecraft.sublog.service;

import dev.arzcbnh.minecraft.sublog.data.TokenRepository;
import java.util.UUID;

public final class TokenService {
    private final TokenGenerator generator;
    private final TokenHasher hasher;
    private final TokenRepository repository;

    public TokenService(TokenGenerator generator, TokenHasher hasher, TokenRepository repository) {
        this.generator = generator;
        this.hasher = hasher;
        this.repository = repository;
    }

    public String issue(UUID playerId) {
        final var token = this.generator.generate();
        this.repository.saveHash(playerId, this.hasher.hash(token));
        return token;
    }

    public void revoke(UUID playerId) {
        this.repository.remove(playerId);
    }

    public boolean isValid(UUID playerId, String token) {
        return this.repository
                .findHash(playerId)
                .filter(hash -> this.hasher.matches(token, hash))
                .isPresent();
    }
}
