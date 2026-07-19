package dev.arzcbnh.minecraft.sublog;

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
public class SubLogCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(SubLogCommands::dispatch);
    }

    @SuppressWarnings("unused")
    private static void dispatch(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext buildContext,
            Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("sublog")
                .then(Commands.literal("token")
                        .then(Commands.literal("generate")
                                .requires(CommandSourceStack::isPlayer)
                                .executes(SubLogCommands::generateTokenCallback))
                        .then(Commands.literal("revoke")
                                .requires(SubLogCommands::isModerator)
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .executes(SubLogCommands::revokeTokenCallback))))
                .then(Commands.literal("passes")
                        .then(Commands.literal("get")
                                .requires(CommandSourceStack::isPlayer)
                                .executes(SubLogCommands::getOwnPassesCallback))
                        .then(Commands.literal("get")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .requires(SubLogCommands::isModerator)
                                        .executes(SubLogCommands::getPassesCallback)))
                        .then(Commands.literal("set")
                                .requires(SubLogCommands::isModerator)
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(SubLogCommands::setPassesCallback))))));
    }

    private static int generateTokenCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final var player = Objects.requireNonNull(source.getPlayer());
        SubLogServerContext.get(server).registration().register(player);
        return 1;
    }

    private static int revokeTokenCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final var name = StringArgumentType.getString(context, "player");
        final var uuid = UUIDUtil.createOfflinePlayerUUID(name);
        SubLogServerContext.get(server).tokens().revoke(uuid);
        source.sendSuccess(() -> Component.translatable("sublog.success.revoke", name), true);
        return 1;
    }

    private static int getOwnPassesCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final var player = Objects.requireNonNull(source.getPlayer());
        final int amount = SubLogServerContext.get(server).passes().get(player.getUUID());
        source.sendSuccess(() -> Component.translatable("sublog.success.get", player.getDisplayName(), amount), false);
        return 1;
    }

    private static int getPassesCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final var name = StringArgumentType.getString(context, "player");
        final var uuid = UUIDUtil.createOfflinePlayerUUID(name);
        final int amount = SubLogServerContext.get(server).passes().get(uuid);
        source.sendSuccess(() -> Component.translatable("sublog.success.get", name, amount), true);
        return 1;
    }

    private static int setPassesCallback(CommandContext<CommandSourceStack> context) {
        final var source = context.getSource();
        final var server = source.getServer();
        final int amount = IntegerArgumentType.getInteger(context, "amount");
        final var name = StringArgumentType.getString(context, "player");
        final var uuid = UUIDUtil.createOfflinePlayerUUID(name);
        SubLogServerContext.get(server).passes().set(uuid, amount);
        source.sendSuccess(() -> Component.translatable("sublog.success.set", name, amount), true);
        return 1;
    }

    private static boolean isModerator(CommandSourceStack source) {
        return source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR);
    }
}
