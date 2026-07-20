package dev.arzcbnh.minecraft.turnstile;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public enum TurnstileCustomClickActions {
    GENERATE_TOKEN("gen-token");

    public final String path;

    TurnstileCustomClickActions(String path) {
        this.path = path;
    }

    public static @Nullable TurnstileCustomClickActions resolve(Identifier id) {
        if (id != null && id.getNamespace().equals(Turnstile.ID) && id.getPath().equals("gen-token")) {
            return GENERATE_TOKEN;
        }

        return null;
    }
}
