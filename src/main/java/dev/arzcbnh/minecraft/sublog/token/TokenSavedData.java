package dev.arzcbnh.minecraft.sublog.token;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.arzcbnh.minecraft.sublog.SubLog;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

public class TokenSavedData extends SavedData {
    public static final int VERSION = 1;

    public static final Codec<TokenSavedData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("Version").forGetter(inst -> inst.version),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.STRING)
                            .fieldOf("Hashes")
                            .forGetter(inst -> inst.hashes))
            .apply(builder, TokenSavedData::new));

    public static final SavedDataType<TokenSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(SubLog.ID, "tokens"),
            () -> new TokenSavedData(VERSION, Map.of()),
            CODEC,
            null);

    private final int version;
    private final Map<UUID, String> hashes;

    private TokenSavedData(int version, Map<UUID, String> hashes) {
        this.version = version;
        this.hashes = new HashMap<>(hashes);
    }

    public static TokenSavedData getInstance(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public void setToken(UUID uuid, @Nullable String token) {
        if (token == null) {
            hashes.remove(uuid);
        } else {
            try {
                final var sha256 = MessageDigest.getInstance("SHA-256");
                final byte[] bytes = sha256.digest(token.getBytes());
                final var hash = Base64.getEncoder().encodeToString(bytes);
                hashes.put(uuid, hash);
            } catch (NoSuchAlgorithmException e) {
                SubLog.LOGGER.error("Failed to hash token", e);
                return;
            }
        }

        this.setDirty();
    }

    public boolean isTokenValid(UUID uuid, String token) {
        final var hash = hashes.get(uuid);

        if (hash == null || token == null) {
            return false;
        }

        try {
            final var sha256 = MessageDigest.getInstance("SHA-256");
            final byte[] bytes = sha256.digest(token.getBytes());
            return MessageDigest.isEqual(bytes, Base64.getDecoder().decode(hash));
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            SubLog.LOGGER.error("Failed to validate token", e);
            return false;
        }
    }
}
