package dev.arzcbnh.minecraft.sublog;

import dev.arzcbnh.minecraft.sublog.data.PassSavedData;
import dev.arzcbnh.minecraft.sublog.data.TokenSavedData;
import dev.arzcbnh.minecraft.sublog.service.AlphanumericTokenGenerator;
import dev.arzcbnh.minecraft.sublog.service.AuthService;
import dev.arzcbnh.minecraft.sublog.service.PassService;
import dev.arzcbnh.minecraft.sublog.service.RegistrationService;
import dev.arzcbnh.minecraft.sublog.service.Sha256TokenHasher;
import dev.arzcbnh.minecraft.sublog.service.TokenService;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.server.MinecraftServer;

public final class SubLogServerContext {
    private static final int TOKEN_LENGTH = 32; // TODO: make configurable
    private static final Map<MinecraftServer, SubLogServerContext> CONTEXTS = new WeakHashMap<>();

    private final AuthService auth;
    private final RegistrationService registration;
    private final TokenService tokens;
    private final PassService passes;

    private SubLogServerContext(MinecraftServer server) {
        // TODO: refactor constructions with dep. injection
        final var tokenRepository = TokenSavedData.get(server);
        final var passRepository = PassSavedData.get(server);
        final var tokenHasher = new Sha256TokenHasher();

        this.tokens = new TokenService(new AlphanumericTokenGenerator(TOKEN_LENGTH), tokenHasher, tokenRepository);
        this.passes = new PassService(passRepository);
        this.auth = new AuthService(this.tokens, this.passes);
        this.registration = new RegistrationService(this.tokens, this.passes, this.auth);
    }

    public static synchronized SubLogServerContext get(MinecraftServer server) {
        return CONTEXTS.computeIfAbsent(server, SubLogServerContext::new);
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
