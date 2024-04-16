package me.munchii.igloolib.command;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.util.ArrayUtil;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class IglooCommandGroup implements CommandExecutor, TabCompleter, Listener {
    private final String groupCommand;
    private final Set<String> groupCommandAliases;
    private boolean enabled;

    private final Set<IglooCommand> subCommands;

    private IglooCommandPermissionHandler permissionHandler = new DefaultCommandPermissionHandler();

    public IglooCommandGroup(String command) {
        this(command, Collections.emptySet());
    }

    public IglooCommandGroup(String command, Set<String> aliases) {
        this.groupCommand = command;
        this.groupCommandAliases = aliases;
        this.enabled = true;
        this.subCommands = new HashSet<>();

        enable();
    }

    public static Builder create(String groupCommand) {
        return new Builder(groupCommand);
    }

    public void enable() {
        if (enabled) return;

        Objects.requireNonNull(Igloolib.INSTANCE.getCommand(groupCommand)).setExecutor(this);
        Objects.requireNonNull(Igloolib.INSTANCE.getCommand(groupCommand)).setTabCompleter(this);
        Objects.requireNonNull(Igloolib.INSTANCE.getCommand(groupCommand)).register(((CraftServer) Igloolib.INSTANCE.getServer()).getCommandMap());
        enableAll();
        enabled = true;
    }

    public void disable() {
        if (!enabled) return;

        Objects.requireNonNull(Igloolib.INSTANCE.getCommand(groupCommand)).setExecutor(null);
        Objects.requireNonNull(Igloolib.INSTANCE.getCommand(groupCommand)).setTabCompleter(null);
        Objects.requireNonNull(Igloolib.INSTANCE.getCommand(groupCommand)).unregister(((CraftServer) Igloolib.INSTANCE.getServer()).getCommandMap());
        disableAll();
        enabled = false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0 && hasCommand(args[0]) && enabled) {
            IglooCommand subCommand = getSubCommand(args[0]);
            if (subCommand.isEnabled()) {
                if (permissionHandler.hasCommandPermission(commandSender, subCommand.getPermission())) {
                    return subCommand.onCommand(commandSender, command, label, ArrayUtil.copyArrayOfRange(args, args.length));
                } else {
                    permissionHandler.onPermissionFailure(commandSender, subCommand);
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (commandSender instanceof Player && enabled) {
            if (args.length > 0 && hasCommand(args[0])) {
                IglooCommand subCommand = getSubCommand(args[0]);
                if (subCommand.isEnabled() && permissionHandler.hasCommandPermission(commandSender, subCommand.getPermission())) {
                    return subCommand.onTabComplete(commandSender, command, label, args);
                }
            } else {
                SortedSet<String> tab = new TreeSet<>();
                for (IglooCommand subCommand : subCommands) {
                    if (!subCommand.isEnabled()) {
                        continue;
                    }

                    List<String> cmdAccess = new ArrayList<>(subCommand.getCommandAliases().stream().toList());
                    cmdAccess.add(subCommand.getCommand());

                    for (String cmd : cmdAccess) {
                        if (cmd.startsWith(args[0]) && !tab.contains(cmd) && permissionHandler.hasCommandPermission(commandSender, subCommand.getPermission())) {
                            tab.add(cmd);
                        }
                    }
                }
                return new ArrayList<>(tab);
            }
        }

        return new ArrayList<>();
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        if (!enabled) {
            return;
        }

        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        List<String> commandAliases = new ArrayList<>();
        groupCommandAliases.forEach(s -> commandAliases.add("/" + s));

        if (args.length >= 2 && (args[0].equals("/" + groupCommand) || commandAliases.contains(args[0]))) {
            if (hasCommand(args[1])) {
                IglooCommand subCommand = getSubCommand(args[1]);
                if (!permissionHandler.hasCommandPermission(event.getPlayer(), subCommand.getPermission())) {
                    permissionHandler.onPermissionFailure(event.getPlayer(), subCommand);
                    event.setCancelled(true);
                }
            }
        }
    }

    public IglooCommandGroup setPermissionHandler(IglooCommandPermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
        return this;
    }

    public IglooCommandGroup registerCommand(IglooCommand subCommand) {
        if (!hasCommand(subCommand)) {
            subCommand.setGroup(this);
            subCommands.add(subCommand);
        }

        return this;
    }

    public IglooCommandGroup registerCommand(Supplier<IglooCommand> subCommand) {
        IglooCommand cmd = subCommand.get();
        if (!hasCommand(cmd)) {
            cmd.setGroup(this);
            subCommands.add(cmd);
        }

        return this;
    }

    public IglooCommandGroup deregisterCommand(IglooCommand subCommand) {
        subCommands.remove(subCommand);
        return this;
    }

    public void enableAll() {
        subCommands.forEach(subCommand -> subCommand.setEnabled(true));
    }

    public void disableAll() {
        subCommands.forEach(subCommand -> subCommand.setEnabled(false));
    }

    public boolean hasCommand(IglooCommand cmd) {
        return hasCommand(cmd.getCommand()) || hasCommand(cmd.getCommandAliases());
    }

    public boolean hasCommand(String cmd) {
        return subCommands.stream()
                .anyMatch(subCommand -> subCommand.getCommand().equals(cmd)
                        || subCommand.getCommandAliases().contains(cmd));
    }

    public boolean hasCommand(Set<String> aliases) {
        return subCommands.stream()
                .anyMatch(subCommand -> aliases.contains(subCommand.getCommand())
                        || subCommand.getCommandAliases().stream().anyMatch(aliases::contains));
    }

    public IglooCommand getSubCommand(String cmd) {
        return subCommands.stream()
                .filter(subCommand -> subCommand.getCommandAliases().contains(cmd) || subCommand.getCommand().equals(cmd))
                .findFirst()
                .orElse(null);
    }

    public IglooCommand getSubCommand(Set<String> aliases) {
        return subCommands.stream()
                .filter(subCommand -> subCommand.getCommandAliases().containsAll(aliases) || aliases.contains(subCommand.getCommand()))
                .findFirst()
                .orElse(null);
    }

    public String getGroupCommand() {
        return groupCommand;
    }

    public Set<String> getGroupCommandAliases() {
        return groupCommandAliases;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<IglooCommand> getSubCommands() {
        return Collections.unmodifiableSet(subCommands);
    }

    public static class Builder {
        private final String groupCommand;

        private Set<String> groupAliases = Collections.emptySet();
        private IglooCommandPermissionHandler permissionHandler;
        private Set<IglooCommand> commands = new HashSet<>();

        public Builder(String groupCommand) {
            this.groupCommand = groupCommand;
        }

        public Builder withAliases(Set<String> groupAliases) {
            this.groupAliases = groupAliases;
            return this;
        }

        public Builder withPermissionHandler(IglooCommandPermissionHandler permissionHandler) {
            this.permissionHandler = permissionHandler;
            return this;
        }

        public Builder withCommand(IglooCommand command) {
            commands.add(command);
            return this;
        }

        public Builder withCommand(Supplier<IglooCommand> command) {
            commands.add(command.get());
            return this;
        }

        public IglooCommandGroup create() {
            final IglooCommandGroup group = new IglooCommandGroup(groupCommand, groupAliases);

            if (permissionHandler != null) {
                group.setPermissionHandler(permissionHandler);
            }

            commands.forEach(group::registerCommand);

            return group;
        }
    }
}
