package dev.arzcbnh.minecraft.sublog.dialog;

import dev.arzcbnh.minecraft.sublog.SubLog;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Optional;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.dialog.*;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.util.CommonColors;
import org.jspecify.annotations.Nullable;

public class TokenGeneratedDialogFactory {
    private final String token;

    @Nullable private final String url;

    public TokenGeneratedDialogFactory(String token) {
        this.token = token;
        this.url = withUrl(token);
    }

    public Dialog create() {
        final var common = this.getCommonData();
        final var action = this.getActionButton();
        return new NoticeDialog(common, action);
    }

    public static Dialog create(String url) {
        return new TokenGeneratedDialogFactory(url).create();
    }

    private CommonDialogData getCommonData() {
        final var title = Component.translatable("sublog.dialog.title");
        final var body = List.of((DialogBody) new PlainMessage(this.getInfo(), PlainMessage.DEFAULT_WIDTH));
        return new CommonDialogData(title, Optional.empty(), false, false, DialogAction.CLOSE, body, List.of());
    }

    private Component getInfo() {
        final String key, data;

        if (this.url == null) {
            key = "sublog.dialog.info.token";
            data = token;
        } else {
            key = "sublog.dialog.info.url";
            data = url;
        }

        final var style = Style.EMPTY.withClickEvent(new ClickEvent.CopyToClipboard(data));
        final var component = Component.literal(data).withStyle(style).withColor(CommonColors.SOFT_YELLOW);
        return Component.translatable(key).append(component);
    }

    private ActionButton getActionButton() {
        final var action = new StaticAction(new ClickEvent.CopyToClipboard(url == null ? token : url));
        final var common = new CommonButtonData(
                Component.translatable("sublog.dialog.action"), (int) (CommonButtonData.DEFAULT_WIDTH * 1.5));

        return new ActionButton(common, Optional.of(action));
    }

    private static @Nullable String withUrl(String token) {
        final var warning = "Could not read URL file, will provide only tokens to players";

        try {
            final var file = FabricLoader.getInstance().getConfigDir().resolve("sublog.txt");
            final var url = Files.readString(file).trim();
            return String.format("%s.%s", token, url);
        } catch (NoSuchFileException e) {
            SubLog.LOGGER.warn(warning);
        } catch (IOException e) {
            SubLog.LOGGER.warn(warning, e);
        }

        return null;
    }
}
