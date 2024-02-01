package me.munchii.igloolib.command;

import org.bukkit.command.CommandSender;

public interface IglooCommandPermissionHandler {
    boolean doesPlayerBypass(CommandSender player);

    boolean hasCommandPermission(CommandSender player, String permission);

    void onPermissionFailure(CommandSender player, IglooCommand command);
}
