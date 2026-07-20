package dev.arzcbnh.minecraft.turnstile;

import dev.arzcbnh.minecraft.turnstile.service.RegistrationService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Turnstile implements ModInitializer {
    public static final String ID = "turnstile";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        TurnstileGameRules.register();
        TurnstileCommands.register();

        ServerPlayerEvents.JOIN.register(RegistrationService::remindIfUnregistered);
    }
}
