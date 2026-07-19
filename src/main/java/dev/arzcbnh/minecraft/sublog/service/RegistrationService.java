package dev.arzcbnh.minecraft.sublog.service;

import dev.arzcbnh.minecraft.sublog.SubLogGameRules;
import dev.arzcbnh.minecraft.sublog.SubLogServerContext;
import dev.arzcbnh.minecraft.sublog.ui.RegistrationReminderMessage;
import dev.arzcbnh.minecraft.sublog.ui.TokenGeneratedDialog;
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

        if (!server.getGameRules().get(SubLogGameRules.ENABLE)) {
            return;
        }

        final var self = SubLogServerContext.get(server).registration();

        if (!self.auth.isAuthenticated(player)) {
            return;
        }

        final var remaining = self.passes.get(player.getUUID());
        final var message = RegistrationReminderMessage.create(remaining);
        player.sendSystemMessage(message);
    }
}
