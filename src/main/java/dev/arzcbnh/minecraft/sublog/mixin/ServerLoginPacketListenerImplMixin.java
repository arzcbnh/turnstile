package dev.arzcbnh.minecraft.sublog.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.arzcbnh.minecraft.sublog.SubLogGameRules;
import dev.arzcbnh.minecraft.sublog.SubLogServerContext;
import dev.arzcbnh.minecraft.sublog.service.AuthResult;
import java.net.SocketAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
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
        final var hostName = this.connection.sublog$getHostName();

        if (error != null
                || hostName == null
                || this.server.usesAuthentication()
                || !this.server.getGameRules().get(SubLogGameRules.ENABLE)) {
            return error;
        }

        final var token = parseToken(hostName);
        final var result = SubLogServerContext.get(this.server).auth().authenticate(nameAndId.id(), token);

        this.connection.sublog$setTokenAuthenticated(result == AuthResult.PASS_ACCEPTED);
        return result == AuthResult.DENIED ? Component.translatable("multiplayer.disconnect.not_whitelisted") : null;
    }

    @Unique private static String parseToken(String hostName) {
        final var index = Math.max(0, hostName.indexOf("."));
        return hostName.substring(0, index);
    }
}
