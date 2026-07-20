package dev.arzcbnh.minecraft.turnstile;

import dev.arzcbnh.minecraft.turnstile.data.PassSavedData;
import dev.arzcbnh.minecraft.turnstile.data.TokenSavedData;
import dev.arzcbnh.minecraft.turnstile.service.AlphanumericTokenGenerator;
import dev.arzcbnh.minecraft.turnstile.service.AuthService;
import dev.arzcbnh.minecraft.turnstile.service.PassService;
import dev.arzcbnh.minecraft.turnstile.service.RegistrationService;
import dev.arzcbnh.minecraft.turnstile.service.Sha256TokenHasher;
import dev.arzcbnh.minecraft.turnstile.service.TokenService;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.server.MinecraftServer;

public final class TurnstileServerContext {
    private static final int TOKEN_LENGTH = 32; // TODO: make configurable
    private static final Map<MinecraftServer, TurnstileServerContext> CONTEXTS = new WeakHashMap<>();

    private final AuthService auth;
    private final RegistrationService registration;
    private final TokenService tokens;
    private final PassService passes;

    private TurnstileServerContext(MinecraftServer server) {
        // TODO: refactor constructions with dep. injection
        final var tokenRepository = TokenSavedData.get(server);
        final var passRepository = PassSavedData.get(server);
        final var tokenHasher = new Sha256TokenHasher();

        this.tokens = new TokenService(new AlphanumericTokenGenerator(TOKEN_LENGTH), tokenHasher, tokenRepository);
        this.passes = new PassService(passRepository);
        this.auth = new AuthService(this.tokens, this.passes);
        this.registration = new RegistrationService(this.tokens, this.passes, this.auth);
    }

    public static synchronized TurnstileServerContext get(MinecraftServer server) {
        return CONTEXTS.computeIfAbsent(server, TurnstileServerContext::new);
    }

    public AuthService auth() {
        return this.auth;
    }

    public RegistrationService registration() {
        return this.registration;
    }

    public TokenService tokens() {
        return this.tokens;
    }

    public PassService passes() {
        return this.passes;
    }
}
