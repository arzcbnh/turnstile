package dev.arzcbnh.minecraft.sublog.service;

import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

public final class AuthService {
    private final TokenService tokens;
    private final PassService passes;

    public AuthService(TokenService tokens, PassService passes) {
        this.tokens = tokens;
        this.passes = passes;
    }

    public AuthResult authenticate(UUID id, String token) {
        if (this.tokens.isValid(id, token)) {
            return AuthResult.TOKEN_ACCEPTED;
        } else if (this.passes.consume(id)) {
            return AuthResult.PASS_ACCEPTED;
        } else {
            return AuthResult.DENIED;
        }
    }

    public boolean isAuthenticated(ServerPlayer player) {
        return player.connection.connection.sublog$isTokenAuthenticated();
    }
}
