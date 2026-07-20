package dev.arzcbnh.minecraft.turnstile.service;

import dev.arzcbnh.minecraft.turnstile.TurnstileGameRules;
import dev.arzcbnh.minecraft.turnstile.TurnstileServerContext;
import dev.arzcbnh.minecraft.turnstile.ui.RegistrationReminderMessage;
import dev.arzcbnh.minecraft.turnstile.ui.TokenGeneratedDialog;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;

public class RegistrationService {
    private final TokenService tokens;
    private final PassService passes;
    private final AuthService auth;

    public RegistrationService(TokenService tokens, PassService passes, AuthService auth) {
        this.tokens = tokens;
        this.passes = passes;
        this.auth = auth;
    }

    public void register(ServerPlayer player) {
        final var token = this.tokens.issue(player.getUUID());
        final var url = ServerHostNameResolver.load().with(token);
        final var dialog = TokenGeneratedDialog.create(url.orElse(token), url.isPresent());
        player.openDialog(Holder.direct(dialog));
    }

    public static void remindIfUnregistered(ServerPlayer player) {
        final var server = player.level().getServer();

        if (!server.getGameRules().get(TurnstileGameRules.ENABLE)) {
            return;
        }

        final var self = TurnstileServerContext.get(server).registration();

        if (!self.auth.isAuthenticated(player)) {
            return;
        }

        final var remaining = self.passes.get(player.getUUID());
        final var message = RegistrationReminderMessage.create(remaining);
        player.sendSystemMessage(message);
    }
}
