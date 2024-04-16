package me.munchii.igloolib.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IglooCommandPermissionHandler {
    boolean doesPlayerBypass(@NotNull CommandSender player);

    boolean hasCommandPermission(@NotNull CommandSender player, @Nullable String permission);

    void onPermissionFailure(@NotNull CommandSender player, @Nullable IglooCommand command);
}
