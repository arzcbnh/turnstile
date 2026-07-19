package dev.arzcbnh.minecraft.sublog.service;

import dev.arzcbnh.minecraft.sublog.data.PassRepository;
import java.util.UUID;

public final class PassService {
    private final PassRepository repository;

    public PassService(PassRepository repository) {
        this.repository = repository;
    }

    public int get(UUID playerId) {
        return this.repository.get(playerId);
    }

    public void set(UUID playerId, int amount) {
        this.repository.set(playerId, Math.max(amount, 0));
    }

    public boolean consume(UUID playerId) {
        final int current = this.repository.get(playerId);
        if (current <= 0) {
            return false;
        }

        this.repository.set(playerId, current - 1);
        return true;
    }
}
