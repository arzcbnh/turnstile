package dev.arzcbnh.minecraft.turnstile.ui;

import dev.arzcbnh.minecraft.turnstile.Turnstile;
import dev.arzcbnh.minecraft.turnstile.TurnstileCustomClickActions;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;

public final class RegistrationReminderMessage {
    private RegistrationReminderMessage() {}

    public static Component create(int remaining) {
        final var command = Component.translatable("turnstile.chat.click")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.BLUE)
                        .withClickEvent(event())
                        .withUnderlined(true));
        final var prefix = Component.literal("[Turnstile] ").withColor(CommonColors.SOFT_RED);
        final var body = Component.translatable("turnstile.chat.warn", remaining, command)
                .withColor(CommonColors.WHITE);

        return prefix.append(body);
    }

    private static ClickEvent event() {
        final var id = Identifier.fromNamespaceAndPath(Turnstile.ID, TurnstileCustomClickActions.GENERATE_TOKEN.path);
        final var payload = Optional.<Tag>empty();
        return new ClickEvent.Custom(id, payload);
    }
}
