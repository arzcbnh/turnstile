package dev.arzcbnh.minecraft.sublog;

public interface ConnectionMixinAccessor {
    String sublog$getHostName();

    void sublog$setHostName(String hostName);

    boolean sublog$isAuthenticated();

    void sublog$setAuthenticated(boolean authenticated);
}
