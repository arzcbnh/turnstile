package dev.arzcbnh.minecraft.sublog.mixin;

import dev.arzcbnh.minecraft.sublog.SubLogCustomClickActions;
import dev.arzcbnh.minecraft.sublog.SubLogServerContext;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin {
    @Shadow
    @Final
    protected MinecraftServer server;

    @Inject(method = "handleCustomClickAction", at = @At("HEAD"))
    private void sublog$handleCustomClickAction(ServerboundCustomClickActionPacket packet, CallbackInfo ci) {
        if (!((ServerCommonPacketListenerImpl) (Object) this instanceof ServerGamePacketListenerImpl self)) {
            return;
        }

        if (SubLogCustomClickActions.resolve(packet.id()) == SubLogCustomClickActions.GENERATE_TOKEN) {
            SubLogServerContext.get(this.server).registration().register(self.player);
        }
    }
}
