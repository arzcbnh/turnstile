package dev.arzcbnh.minecraft.turnstile.data;

import java.util.UUID;

public interface PassRepository {
    int get(UUID playerId);

    void set(UUID playerId, int amount);
}
