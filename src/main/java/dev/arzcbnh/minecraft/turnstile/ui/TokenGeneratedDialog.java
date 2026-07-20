package dev.arzcbnh.minecraft.turnstile.ui;

import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.dialog.DialogAction;
import net.minecraft.server.dialog.NoticeDialog;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.util.CommonColors;

public final class TokenGeneratedDialog {
    private TokenGeneratedDialog() {}

    public static Dialog create(String value, boolean isUrl) {
        final var title = Component.translatable("turnstile.dialog.title");
        final var body =
                List.of((DialogBody) new PlainMessage(info(value, isUrl), (int) (PlainMessage.DEFAULT_WIDTH * 1.5)));
        final var common =
                new CommonDialogData(title, Optional.empty(), false, false, DialogAction.CLOSE, body, List.of());
        return new NoticeDialog(common, actionButton(value));
    }

    private static Component info(String value, boolean isUrl) {
        final var key = isUrl ? "turnstile.dialog.info.url" : "turnstile.dialog.info.token";
        final var style = Style.EMPTY.withClickEvent(new ClickEvent.CopyToClipboard(value));
        final var data = Component.literal("\n\n" + value).withStyle(style).withColor(CommonColors.SOFT_YELLOW);
        return Component.translatable(key).append(data);
    }

    private static ActionButton actionButton(String value) {
        final var action = new StaticAction(new ClickEvent.CopyToClipboard(value));
        final var common = new CommonButtonData(
                Component.translatable("turnstile.dialog.action"), (int) (CommonButtonData.DEFAULT_WIDTH * 1.5));
        return new ActionButton(common, Optional.of(action));
    }
}
