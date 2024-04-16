package me.munchii.igloolib.command;

import me.munchii.igloolib.player.IglooPlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class CommandExecutionContext {
    private final IglooPlayer player;
    private final IglooCommand command;
    private final List<String> args;

    protected CommandExecutionContext(@NotNull final IglooPlayer player, @NotNull final IglooCommand command, @NotNull final String[] args) {
        this.player = player;
        this.command = command;
        this.args = List.of(args);
    }

    @NotNull
    public final IglooPlayer getPlayer() {
        return player;
    }

    @NotNull
    public final World getWorld() {
        return player.getWorld();
    }

    @NotNull
    public final CommandSender getCommandSender() {
        return Objects.requireNonNull(player.getCommandSender());
    }

    @NotNull
    public final IglooCommand getCommand() {
        return command;
    }

    public boolean isCommandGroup() {
        return command.getGroup() != null;
    }

    @NotNull
    public final List<String> getArgs() {
        return args;
    }
}
