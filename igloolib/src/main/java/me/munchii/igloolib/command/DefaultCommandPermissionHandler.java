package me.munchii.igloolib.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultCommandPermissionHandler implements IglooCommandPermissionHandler {
    @Override
    public boolean doesPlayerBypass(@NotNull CommandSender player) {
        return player.isOp();
    }

    @Override
    public boolean hasCommandPermission(@NotNull CommandSender player, @Nullable String permission) {
        if (permission == null) return true;
        return permission.isEmpty() || doesPlayerBypass(player) || player.hasPermission(permission);
    }

    @Override
    public void onPermissionFailure(@NotNull CommandSender player, @Nullable IglooCommand command) {

    }
}
