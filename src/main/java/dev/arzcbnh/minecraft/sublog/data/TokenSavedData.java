package dev.arzcbnh.minecraft.sublog.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.arzcbnh.minecraft.sublog.SubLog;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public final class TokenSavedData extends SavedData implements TokenRepository {
    private static final int VERSION = 1;

    private static final Codec<TokenSavedData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("Version").forGetter(data -> data.version),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.STRING)
                            .fieldOf("Hashes")
                            .forGetter(data -> data.hashes))
            .apply(builder, TokenSavedData::new));

    @SuppressWarnings("DataFlowIssue")
    private static final SavedDataType<TokenSavedData> TYPE = new SavedDataType<>(
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

    public static TokenSavedData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    @Override
    public Optional<String> findHash(UUID playerId) {
        return Optional.ofNullable(this.hashes.get(playerId));
    }

    @Override
    public void saveHash(UUID playerId, String encodedHash) {
        this.hashes.put(playerId, encodedHash);
        this.setDirty();
    }

    @Override
    public void remove(UUID playerId) {
        if (this.hashes.remove(playerId) != null) {
            this.setDirty();
        }
    }
}
