package dev.arzcbnh.minecraft.turnstile.mixin;

import dev.arzcbnh.minecraft.turnstile.tweaker.ConnectionAuthStateHolder;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Connection.class)
public class ConnectionMixin implements ConnectionAuthStateHolder {
    @Unique private String turnstile$hostName;

    @Unique private boolean turnstile$tokenAuthenticated = false;

    @Override
    public String turnstile$getHostName() {
        return this.turnstile$hostName;
    }

    @Override
    public void turnstile$setHostName(String hostName) {
        this.turnstile$hostName = hostName;
    }

    @Override
    public boolean turnstile$isTokenAuthenticated() {
        return this.turnstile$tokenAuthenticated;
    }

    @Override
    public void turnstile$setTokenAuthenticated(boolean authenticated) {
        this.turnstile$tokenAuthenticated = authenticated;
    }
}
