package dev.arzcbnh.minecraft.sublog.mixin;

import dev.arzcbnh.minecraft.sublog.ConnectionMixinAccessor;
import dev.arzcbnh.minecraft.sublog.ServerPlayerMixinAccessor;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.config.PrepareSpawnTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrepareSpawnTask.class)
public class PrepareSpawnTaskMixin {
    @Inject(method = "spawnPlayer", at = @At(value = "RETURN"))
    private void sublog$setAuthenticated(Connection connection, CommonListenerCookie cookie, CallbackInfoReturnable<ServerPlayer> cir) {
        ((ServerPlayerMixinAccessor) cir.getReturnValue()).sublog$setAuthenticated(((ConnectionMixinAccessor) connection).sublog$isAuthenticated());
    }
}
