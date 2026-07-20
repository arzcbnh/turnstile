package dev.arzcbnh.minecraft.turnstile.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.arzcbnh.minecraft.turnstile.Turnstile;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public final class PassSavedData extends SavedData implements PassRepository {
    private static final int VERSION = 1;

    private static final Codec<PassSavedData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("Version").forGetter(data -> data.version),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT)
                            .fieldOf("Passes")
                            .forGetter(data -> data.passes))
            .apply(builder, PassSavedData::new));

    @SuppressWarnings("DataFlowIssue")
    private static final SavedDataType<PassSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(Turnstile.ID, "passes"),
            () -> new PassSavedData(VERSION, Map.of()),
            CODEC,
            null);

    private final int version;
    private final Map<UUID, Integer> passes;

    private PassSavedData(int version, Map<UUID, Integer> passes) {
        this.version = version;
        this.passes = new HashMap<>(passes);
    }

    public static PassSavedData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    @Override
    public int get(UUID playerId) {
        return this.passes.getOrDefault(playerId, 0);
    }

    @Override
    public void set(UUID playerId, int amount) {
        if (amount <= 0) {
            this.passes.remove(playerId);
        } else {
            this.passes.put(playerId, amount);
        }
        this.setDirty();
    }
}
