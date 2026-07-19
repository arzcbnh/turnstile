package dev.arzcbnh.minecraft.sublog.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.arzcbnh.minecraft.sublog.ConnectionMixinAccessor;
import dev.arzcbnh.minecraft.sublog.SubLogGameRules;
import dev.arzcbnh.minecraft.sublog.pass.PassSavedData;
import dev.arzcbnh.minecraft.sublog.token.TokenSavedData;
import java.net.SocketAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    @Final
    private Connection connection;

    @WrapOperation(
            method = "verifyLoginAndFinishConnectionSetup",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/players/PlayerList;canPlayerLogin(Ljava/net/SocketAddress;Lnet/minecraft/server/players/NameAndId;)Lnet/minecraft/network/chat/Component;"))
    private Component sublog$canPlayerLogin(
            PlayerList instance, SocketAddress address, NameAndId nameAndId, Operation<Component> original) {
        final var error = original.call(instance, address, nameAndId);

        if (error != null
                || this.server.usesAuthentication()
                || !this.server.getGameRules().get(SubLogGameRules.ENABLE)) {
            return error;
        }

        final var tokenData = TokenSavedData.getInstance(this.server);
        final var passData = PassSavedData.getInstance(this.server);
        final var token = this.getToken();

        if (tokenData.isTokenValid(nameAndId.id(), token)) {
            ((ConnectionMixinAccessor) this.connection).sublog$setAuthenticated(true);
            return null;
        } else if (passData.consumePass(nameAndId.id())) {
            return null;
        } else {
            return Component.translatable("multiplayer.disconnect.not_whitelisted");
        }
    }

    @Unique private @Nullable String getToken() {
        final var hostName = ((ConnectionMixinAccessor) this.connection).sublog$getHostName();

        try {
            return hostName.substring(0, hostName.indexOf('.'));
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }
}
