package dev.arzcbnh.minecraft.turnstile;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class TurnstileGameRules {
    public static final GameRule<Boolean> ENABLE = GameRuleBuilder.forBoolean(true)
            .category(GameRuleCategory.MISC)
            .buildAndRegister(Identifier.fromNamespaceAndPath(Turnstile.ID, "enable"));

    @SuppressWarnings("EmptyMethod")
    public static void register() {}
}
