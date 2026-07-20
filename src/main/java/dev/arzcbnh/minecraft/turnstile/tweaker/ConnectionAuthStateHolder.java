package dev.arzcbnh.minecraft.turnstile.tweaker;

import org.jspecify.annotations.Nullable;

public interface ConnectionAuthStateHolder {
    default @Nullable String turnstile$getHostName() {
        throw new UnsupportedOperationException();
    }

    default void turnstile$setHostName(String hostName) {
        throw new UnsupportedOperationException();
    }

    default boolean turnstile$isTokenAuthenticated() {
        throw new UnsupportedOperationException();
    }

    default void turnstile$setTokenAuthenticated(boolean authenticated) {
        throw new UnsupportedOperationException();
    }
}
