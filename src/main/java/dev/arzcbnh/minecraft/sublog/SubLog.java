package dev.arzcbnh.minecraft.sublog;

import dev.arzcbnh.minecraft.sublog.dialog.TokenGeneratedDialogFactory;
import dev.arzcbnh.minecraft.sublog.pass.PassSavedData;
import dev.arzcbnh.minecraft.sublog.token.AlphanumericTokenProvider;
import dev.arzcbnh.minecraft.sublog.token.TokenSavedData;
import java.util.Optional;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.PrimitiveTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.CommonColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubLog implements ModInitializer {
    public static final String ID = "sublog";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        SubLogCommands.init();
        SubLogGameRules.init();

        ServerPlayerEvents.JOIN.register(SubLog::notifyPasses);
    }

    private static void notifyPasses(ServerPlayer player) {
        final var server = player.level().getServer();
        final int passes = PassSavedData.getInstance(server).getPasses(player.getUUID());

        if (server.getGameRules().get(SubLogGameRules.ENABLE)
                && !((ServerPlayerMixinAccessor) player).sublog$isAuthenticated()) {
            final var payload = Optional.of((Tag) StringTag.valueOf(player.getUUID().toString()));
            final var gen = new ClickEvent.Custom(Identifier.fromNamespaceAndPath(ID, "gen-token"), payload);
            final var title = Component.literal("[SubLog] ").withColor(CommonColors.SOFT_RED);
            final var command = Component.translatable("sublog.chat.click")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.BLUE)
                            .withUnderlined(true)
                            .withClickEvent(gen));
            final var body =
                    Component.translatable("sublog.chat.warn", passes, command).withColor(CommonColors.WHITE);
            player.sendSystemMessage(title.append(body));
        }
    }

    public static void registerPlayer(ServerPlayer player) {
        // Generate and save token
        final var token = new AlphanumericTokenProvider().generate(32);
        final var data = TokenSavedData.getInstance(player.level().getServer());
        data.setToken(player.getUUID(), token);

        // Display dialog about generated token to player
        final var dialog = TokenGeneratedDialogFactory.create(token);
        player.openDialog(Holder.direct(dialog));
    }
}
