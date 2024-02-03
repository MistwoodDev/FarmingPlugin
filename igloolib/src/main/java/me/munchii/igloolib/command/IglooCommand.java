package me.munchii.igloolib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

public abstract class IglooCommand implements CommandExecutor, TabCompleter {
    private final String command;
    private final Set<String> aliases;
    private final String permission;
    private final String description;
    private final String usage;
    private boolean enabled;

    public IglooCommand(String command) {
        this(command, new HashSet<>(), "", "command /" + command);
    }

    public IglooCommand(String command, Set<String> aliases) {
        this(command, aliases, "", "command /" + command);
    }

    public IglooCommand(String command, Set<String> aliases, String permission) {
        this(command, aliases, permission, "command /" + command);
    }

    public IglooCommand(String command, Set<String> aliases, String permission, String description) {
        this(command, aliases, permission, description, "/" + command);
    }

    public IglooCommand(String command, Set<String> aliases, String permission, String description, String usage) {
        this.command = command;
        this.aliases = aliases;
        this.permission = permission;
        this.description = description;
        this.usage = usage;
    }

    public static Builder create(String command) {
        return new Builder(command);
    }

    public String getCommand() {
        return command;
    }

    public Set<String> getCommandAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission() {
        return permission != null && permission.isEmpty();
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static final class Builder {
        private final String command;
        private final Set<String> aliases;
        private String permission;
        private String description;
        private String usage;
        private BiFunction<CommandSender, List<String>, Boolean> onCommand;
        private BiFunction<CommandSender, List<String>, List<String>> tabComplete;

        public Builder(String cmd) {
            this.command = cmd;
            this.aliases = new HashSet<>();
        }

        public Builder withAlias(String alias) {
            this.aliases.add(alias);
            return this;
        }

        public Builder withPermission(String permission) {
            this.permission = permission;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withUsage(String usage) {
            this.usage = usage;
            return this;
        }

        public Builder withAction(BiFunction<CommandSender, List<String>, Boolean> onCommand) {
            this.onCommand = onCommand;
            return this;
        }

        public Builder withTabComplete(BiFunction<CommandSender, List<String>, List<String>> tabComplete) {
            this.tabComplete = tabComplete;
            return this;
        }

        public IglooCommand build() {
            return new IglooCommand(command, aliases, permission, description, usage) {
                @Override
                public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
                    return onCommand.apply(sender, Arrays.asList(args));
                }

                @Nullable
                @Override
                public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
                    return tabComplete.apply(sender, Arrays.asList(args));
                }
            };
        }
    }
}
