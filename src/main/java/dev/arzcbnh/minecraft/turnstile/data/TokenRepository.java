package dev.arzcbnh.minecraft.turnstile.data;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {
    Optional<String> findHash(UUID playerId);

    void saveHash(UUID playerId, String encodedHash);

    void remove(UUID playerId);
}
