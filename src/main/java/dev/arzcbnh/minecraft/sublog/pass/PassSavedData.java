package dev.arzcbnh.minecraft.sublog.pass;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.arzcbnh.minecraft.sublog.SubLog;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class PassSavedData extends SavedData {
    public static final int VERSION = 1;

    public static final Codec<PassSavedData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("Version").forGetter(inst -> inst.version),
                    Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT)
                            .fieldOf("Passes")
                            .forGetter(inst -> inst.passes))
            .apply(builder, PassSavedData::new));

    public static final SavedDataType<PassSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(SubLog.ID, "passes"),
            () -> new PassSavedData(VERSION, Map.of()),
            CODEC,
            null);

    private final int version;
    private final Map<UUID, Integer> passes;

    private PassSavedData(int version, Map<UUID, Integer> passes) {
        this.version = version;
        this.passes = new HashMap<>(passes);
    }

    public static PassSavedData getInstance(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public int getPasses(UUID uuid) {
        return passes.getOrDefault(uuid, 0);
    }

    public void setPasses(UUID uuid, int amount) {
        if (amount <= 0) {
            passes.remove(uuid);
        } else {
            passes.put(uuid, amount);
        }

        this.setDirty();
    }

    public boolean consumePass(UUID uuid) {
        final int current = this.getPasses(uuid);

        if (current <= 0) {
            return false;
        }

        this.setPasses(uuid, current - 1);
        return true;
    }
}
