package dev.arzcbnh.minecraft.sublog.mixin;

import dev.arzcbnh.minecraft.sublog.SubLog;
import java.util.Optional;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow
    private PlayerList playerList;

    @Inject(method = "handleCustomClickAction", at = @At("HEAD"))
    private void sublog$handleCustomClickAction(Identifier id, Optional<Tag> payload, CallbackInfo ci) {
        if (!id.getNamespace().equals("sublog") || !id.getPath().equals("gen-token") || payload.isEmpty()) {
            return;
        }

        final var player = this.playerList.getPlayer(payload.get().asString().orElse(""));

        if (player != null) {
            SubLog.registerPlayer(player);
        }
    }
}
