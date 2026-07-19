package dev.arzcbnh.minecraft.sublog;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public enum SubLogCustomClickActions {
    GENERATE_TOKEN("gen-token");

    public final String path;

    SubLogCustomClickActions(String path) {
        this.path = path;
    }

    public static @Nullable SubLogCustomClickActions resolve(Identifier id) {
        if (id != null && id.getNamespace().equals(SubLog.ID) && id.getPath().equals("gen-token")) {
            return GENERATE_TOKEN;
        }

        return null;
    }
}
