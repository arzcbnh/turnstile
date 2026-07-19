package dev.arzcbnh.minecraft.sublog.data;

import java.util.UUID;

public interface PassRepository {
    int get(UUID playerId);

    void set(UUID playerId, int amount);
}
