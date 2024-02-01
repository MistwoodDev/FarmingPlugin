package me.munchii.igloolib.command;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.util.ArrayUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class CommandManager implements CommandExecutor, TabCompleter, Listener {
    private final Set<IglooCommand> commands;
    private final Set<IglooCommandGroup> commandGroups;

    private IglooCommandPermissionHandler permissionHandler;

    private boolean enabled;

    public CommandManager() {
        this.commands = new HashSet<>();
        this.commandGroups = new HashSet<>();

        this.permissionHandler = new DefaultCommandPermissionHandler();
        enable();
    }

    public void enable() {
        if (enabled) return;

        commands.forEach(command -> {
            Objects.requireNonNull(Igloolib.INSTANCE.getCommand(command.getCommand())).setExecutor(this);
            Objects.requireNonNull(Igloolib.INSTANCE.getCommand(command.getCommand())).setTabCompleter(this);
            Objects.requireNonNull(Igloolib.INSTANCE.getCommand(command.getCommand())).register(((CraftServer) Igloolib.INSTANCE.getServer()).getCommandMap());
            command.setEnabled(true);
        });

        commandGroups.forEach(IglooCommandGroup::enable);
        Bukkit.getServer().getPluginManager().registerEvents(this, Igloolib.INSTANCE);
        enabled = true;
    }

    public void disable() {
        if (!enabled) return;

        commands.forEach(command -> {
            Objects.requireNonNull(Igloolib.INSTANCE.getCommand(command.getCommand())).setExecutor(null);
            Objects.requireNonNull(Igloolib.INSTANCE.getCommand(command.getCommand())).setTabCompleter(null);
            Objects.requireNonNull(Igloolib.INSTANCE.getCommand(command.getCommand())).unregister(((CraftServer) Igloolib.INSTANCE.getServer()).getCommandMap());
            command.setEnabled(false);
        });

        commandGroups.forEach(IglooCommandGroup::disable);
        HandlerList.unregisterAll(this);
        enabled = false;
    }

    public CommandManager setPermissionHandler(IglooCommandPermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
        return this;
    }

    public CommandManager registerCommand(IglooCommand command) {
        commands.add(command);
        return this;
    }

    public CommandManager registerCommand(Supplier<IglooCommand> command) {
        commands.add(command.get());
        return this;
    }

    public CommandManager registerCommandGroup(IglooCommandGroup group) {
        commandGroups.add(group);
        return this;
    }

    public CommandManager registerCommandGroup(Supplier<IglooCommandGroup> group) {
        commandGroups.add(group.get());
        return this;
    }

    public boolean hasCommand(String cmd) {
        return commands.stream().anyMatch(command -> command.getCommand().equals(cmd) || command.getCommandAliases().contains(cmd));// || commandGroups.stream().anyMatch(group -> group.getGroupCommand().equals(cmd) || group.getGroupCommandAliases().contains(cmd));
    }

    public Optional<IglooCommand> getCommand(String cmd) {
        return commands.stream().filter(command -> command.getCommand().equals(cmd) || command.getCommandAliases().contains(cmd)).findFirst();
    }

    public Optional<IglooCommandGroup> getCommandGroup(String baseCommand) {
        return commandGroups.stream().filter(command -> command.getGroupCommand().equals(baseCommand) || command.getGroupCommandAliases().contains(baseCommand)).findFirst();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0 && hasCommand(args[0])) {
            IglooCommand subCommand = getCommand(args[0]).get();
            return subCommand.onCommand(sender, command, label, ArrayUtil.copyArrayOfRange(args, args.length));
        }

        return true;
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        List<String> commandAliases = new ArrayList<>();
        commands.forEach(command -> {
            commandAliases.add("/" + command.getCommand());
            command.getCommandAliases().forEach(alias -> commandAliases.add("/" + alias));
        });

        if (args.length >= 2 && commandAliases.contains(args[0])) {
            if (hasCommand(args[1])) {
                IglooCommand subCommand = getCommand(args[1]).get();
                if (!permissionHandler.hasCommandPermission(event.getPlayer(), subCommand.getPermission())) {
                    permissionHandler.onPermissionFailure(event.getPlayer(), subCommand);
                    event.setCancelled(true);
                }
            }
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && enabled) {
            if (args.length > 0 && hasCommand(args[0])) {
                IglooCommand subCommand = getCommand(args[0]).get();
                if (subCommand.isEnabled() && permissionHandler.hasCommandPermission(sender, subCommand.getPermission())) {
                    return subCommand.onTabComplete(sender, command, label, args);
                }
            } else {
                SortedSet<String> tab = new TreeSet<>();
                for (IglooCommand subCommand : commands) {
                    if (!subCommand.isEnabled()) {
                        continue;
                    }

                    List<String> cmdAccess = new ArrayList<>(subCommand.getCommandAliases().stream().toList());
                    cmdAccess.add(subCommand.getCommand());

                    for (String cmd : cmdAccess) {
                        if (cmd.startsWith(args[0]) && !tab.contains(cmd) && permissionHandler.hasCommandPermission(sender, subCommand.getPermission())) {
                            tab.add(cmd);
                        }
                    }
                }
                return new ArrayList<>(tab);
            }
        }

        return new ArrayList<>();
    }

    public Set<IglooCommand> getCommands() {
        return Collections.unmodifiableSet(commands);
    }

    public Set<IglooCommandGroup> getCommandGroups() {
        return Collections.unmodifiableSet(commandGroups);
    }
}
