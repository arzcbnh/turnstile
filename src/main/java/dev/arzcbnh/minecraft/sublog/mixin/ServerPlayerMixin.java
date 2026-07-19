package dev.arzcbnh.minecraft.sublog.mixin;

import dev.arzcbnh.minecraft.sublog.ServerPlayerMixinAccessor;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements ServerPlayerMixinAccessor {
    @Unique
    private boolean sublog$isAuthenticated = false;

    @Override
    public boolean sublog$isAuthenticated() {
        return this.sublog$isAuthenticated;
    }

    @Override
    public void sublog$setAuthenticated(boolean authenticated) {
        this.sublog$isAuthenticated = authenticated;
    }
}
