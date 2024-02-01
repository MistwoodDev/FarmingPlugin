package me.munchii.igloolib.command;

import org.bukkit.command.CommandSender;

public class DefaultCommandPermissionHandler implements IglooCommandPermissionHandler {
    @Override
    public boolean doesPlayerBypass(CommandSender player) {
        return player.isOp();
    }

    @Override
    public boolean hasCommandPermission(CommandSender player, String permission) {
        return permission.isEmpty() || doesPlayerBypass(player) || player.hasPermission(permission);
    }

    @Override
    public void onPermissionFailure(CommandSender player, IglooCommand command) {

    }
}
