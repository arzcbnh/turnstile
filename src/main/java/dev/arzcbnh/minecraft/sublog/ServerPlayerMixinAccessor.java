package dev.arzcbnh.minecraft.sublog;

public interface ServerPlayerMixinAccessor {
    boolean sublog$isAuthenticated();
    void sublog$setAuthenticated(boolean authenticated);
}
