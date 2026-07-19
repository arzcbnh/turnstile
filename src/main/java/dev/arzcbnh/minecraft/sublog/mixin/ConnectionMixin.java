package dev.arzcbnh.minecraft.sublog.mixin;

import dev.arzcbnh.minecraft.sublog.tweaker.ConnectionAuthStateHolder;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Connection.class)
public class ConnectionMixin implements ConnectionAuthStateHolder {
    @Unique private String sublog$hostName;

    @Unique private boolean sublog$tokenAuthenticated = false;

    @Override
    public String sublog$getHostName() {
        return this.sublog$hostName;
    }

    @Override
    public void sublog$setHostName(String hostName) {
        this.sublog$hostName = hostName;
    }

    @Override
    public boolean sublog$isTokenAuthenticated() {
        return this.sublog$tokenAuthenticated;
    }

    @Override
    public void sublog$setTokenAuthenticated(boolean authenticated) {
        this.sublog$tokenAuthenticated = authenticated;
    }
}
