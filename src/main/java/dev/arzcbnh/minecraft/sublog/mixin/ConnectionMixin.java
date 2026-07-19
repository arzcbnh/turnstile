package dev.arzcbnh.minecraft.sublog.mixin;

import dev.arzcbnh.minecraft.sublog.ConnectionMixinAccessor;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Connection.class)
public class ConnectionMixin implements ConnectionMixinAccessor {
    @Unique private String sublog$hostName;

    @Unique private boolean sublog$isAuthenticated = false;

    @Override
    public String sublog$getHostName() {
        return this.sublog$hostName;
    }

    @Override
    public void sublog$setHostName(String hostName) {
        this.sublog$hostName = hostName;
    }

    @Override
    public boolean sublog$isAuthenticated() {
        return this.sublog$isAuthenticated;
    }

    @Override
    public void sublog$setAuthenticated(boolean authenticated) {
        this.sublog$isAuthenticated = authenticated;
    }
}
