package dev.arzcbnh.minecraft.sublog.tweaker;

import org.jspecify.annotations.Nullable;

public interface ConnectionAuthStateHolder {
    default @Nullable String sublog$getHostName() {
        throw new UnsupportedOperationException();
    }

    default void sublog$setHostName(String hostName) {
        throw new UnsupportedOperationException();
    }

    default boolean sublog$isTokenAuthenticated() {
        throw new UnsupportedOperationException();
    }

    default void sublog$setTokenAuthenticated(boolean authenticated) {
        throw new UnsupportedOperationException();
    }
}
