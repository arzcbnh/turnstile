package dev.arzcbnh.minecraft.turnstile.service;

import dev.arzcbnh.minecraft.turnstile.Turnstile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Optional;
import net.fabricmc.loader.api.FabricLoader;
import org.jspecify.annotations.Nullable;

public final class ServerHostNameResolver {
    private final @Nullable String domain;

    private ServerHostNameResolver(@Nullable String domain) {
        this.domain = domain;
    }

    public static ServerHostNameResolver load() {
        final Path file = FabricLoader.getInstance().getConfigDir().resolve("turnstile.txt");
        try {
            final var value = Files.readString(file).trim();
            if (value.isEmpty()) {
                Turnstile.LOGGER.warn("URL file {} is empty; players will receive plain tokens", file);
                return new ServerHostNameResolver(null);
            }
            return new ServerHostNameResolver(value);
        } catch (NoSuchFileException exception) {
            Turnstile.LOGGER.warn("URL file {} does not exist; players will receive plain tokens", file);
        } catch (IOException exception) {
            Turnstile.LOGGER.warn("Could not read URL file {}; players will receive plain tokens", file, exception);
        }

        return new ServerHostNameResolver(null);
    }

    public Optional<String> with(String token) {
        return Optional.ofNullable(this.domain).map(value -> String.format("%s.%s", token, value));
    }
}
