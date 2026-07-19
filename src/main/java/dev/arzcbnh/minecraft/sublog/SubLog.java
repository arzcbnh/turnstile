package dev.arzcbnh.minecraft.sublog;

import dev.arzcbnh.minecraft.sublog.service.RegistrationService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SubLog implements ModInitializer {
    public static final String ID = "sublog";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        SubLogGameRules.register();
        SubLogCommands.register();

        ServerPlayerEvents.JOIN.register(RegistrationService::remindIfUnregistered);
    }
}
