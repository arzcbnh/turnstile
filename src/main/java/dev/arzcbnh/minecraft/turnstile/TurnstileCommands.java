package dev.arzcbnh.minecraft.turnstile;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import java.util.Objects;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

@SuppressWarnings("SameReturnValue")
public class TurnstileCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(TurnstileCommands::dispatch);
    }

    @SuppressWarnings("unused")
    private static void dispatch(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext buildContext,
            Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("turnstile")
                .then(Commands.literal("token")
                        .then(Commands.literal("generate")
                                .requires(CommandSourceStack::isPlayer)
                                .executes(TurnstileCommands::generateTokenCallback))
                        .then(Commands.literal("revoke")
                                .requires(TurnstileCommands::isModerator)
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .executes(TurnstileCommands::revokeTokenCallback))))
                .then(Commands.literal("passes")
                        .then(Commands.literal("get")
                                .requires(CommandSourceStack::isPlayer)
                                .executes(TurnstileCommands::getOwnPassesCallback))
                        .then(Commands.literal("get")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .requires(TurnstileCommands::isModerator)
                                        .executes(TurnstileCommands::getPassesCallback)))
                        .then(Commands.literal("set")
                                .requires(TurnstileCommands::isModerator)
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(TurnstileCommands::setPassesCallback))))));
    }

    private static int generateTokenCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final var player = Objects.requireNonNull(source.getPlayer());
        TurnstileServerContext.get(server).registration().register(player);
        return 1;
    }

    private static int revokeTokenCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final var name = StringArgumentType.getString(context, "player");
        final var uuid = UUIDUtil.createOfflinePlayerUUID(name);
        TurnstileServerContext.get(server).tokens().revoke(uuid);
        source.sendSuccess(() -> Component.translatable("turnstile.success.revoke", name), true);
        return 1;
    }

    private static int getOwnPassesCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final var player = Objects.requireNonNull(source.getPlayer());
        final int amount = TurnstileServerContext.get(server).passes().get(player.getUUID());
        source.sendSuccess(
                () -> Component.translatable("turnstile.success.get", player.getDisplayName(), amount), false);
        return 1;
    }

    private static int getPassesCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final var name = StringArgumentType.getString(context, "player");
        final var uuid = UUIDUtil.createOfflinePlayerUUID(name);
        final int amount = TurnstileServerContext.get(server).passes().get(uuid);
        source.sendSuccess(() -> Component.translatable("turnstile.success.get", name, amount), true);
        return 1;
    }

    private static int setPassesCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final int amount = IntegerArgumentType.getInteger(context, "amount");
        final var name = StringArgumentType.getString(context, "player");
        final var uuid = UUIDUtil.createOfflinePlayerUUID(name);
        TurnstileServerContext.get(server).passes().set(uuid, amount);
        source.sendSuccess(() -> Component.translatable("turnstile.success.set", name, amount), true);
        return 1;
    }

    private static boolean isModerator(CommandSourceStack source) {
        return source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR);
    }
}
