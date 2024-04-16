package me.munchii.igloolib.command;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.util.ArrayUtil;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

// TODO: test / handle command groups
// TODO: optimize and make the system prettier
// TODO: more testing?

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

        commands.forEach(this::linkCommand);
        commandGroups.forEach(this::linkCommandGroup);

        Bukkit.getServer().getPluginManager().registerEvents(this, Igloolib.INSTANCE);
        enabled = true;
    }

    public void disable() {
        if (!enabled) return;

        commands.forEach(this::unlinkCommand);
        commandGroups.forEach(this::unlinkCommandGroup);

        HandlerList.unregisterAll(this);
        enabled = false;
    }

    private void linkCommand(IglooCommand command) {
        Objects.requireNonNull(Bukkit.getPluginCommand(command.getCommand())).setExecutor(this);
        Objects.requireNonNull(Bukkit.getPluginCommand(command.getCommand())).setTabCompleter(this);
        Objects.requireNonNull(Bukkit.getPluginCommand(command.getCommand())).register(((CraftServer) Igloolib.INSTANCE.getServer()).getCommandMap());
        command.setEnabled(true);
    }

    private void unlinkCommand(IglooCommand command) {
        Objects.requireNonNull(Bukkit.getPluginCommand(command.getCommand())).setExecutor(null);
        Objects.requireNonNull(Bukkit.getPluginCommand(command.getCommand())).setTabCompleter(null);
        Objects.requireNonNull(Bukkit.getPluginCommand(command.getCommand())).unregister(((CraftServer) Igloolib.INSTANCE.getServer()).getCommandMap());
        command.setEnabled(false);
    }

    private void linkCommandGroup(IglooCommandGroup commandGroup) {
        commandGroup.setEnabled(false);
        commandGroup.enable();
        Objects.requireNonNull(Bukkit.getPluginCommand(commandGroup.getGroupCommand())).setExecutor(this);
        Objects.requireNonNull(Bukkit.getPluginCommand(commandGroup.getGroupCommand())).setTabCompleter(this);
        Objects.requireNonNull(Bukkit.getPluginCommand(commandGroup.getGroupCommand())).register(((CraftServer) Igloolib.INSTANCE.getServer()).getCommandMap());
        commandGroup.setPermissionHandler(permissionHandler);

        //commandGroup.enable();
        Bukkit.getServer().getPluginManager().registerEvents(commandGroup, Igloolib.INSTANCE);
    }

    private void unlinkCommandGroup(IglooCommandGroup commandGroup) {
        commandGroup.setEnabled(false);
        commandGroup.disable();
        Objects.requireNonNull(Bukkit.getPluginCommand(commandGroup.getGroupCommand())).setExecutor(null);
        Objects.requireNonNull(Bukkit.getPluginCommand(commandGroup.getGroupCommand())).setTabCompleter(null);
        Objects.requireNonNull(Bukkit.getPluginCommand(commandGroup.getGroupCommand())).unregister(((CraftServer) Igloolib.INSTANCE.getServer()).getCommandMap());
        commandGroup.setPermissionHandler(permissionHandler);

        //commandGroup.disable();
        HandlerList.unregisterAll(commandGroup);
    }

    public CommandManager setPermissionHandler(IglooCommandPermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
        return this;
    }

    public CommandManager registerCommand(IglooCommand command) {
        commands.add(command);
        linkCommand(command);
        return this;
    }

    public CommandManager registerCommand(Supplier<IglooCommand> command) {
        return registerCommand(command.get());
    }

    public CommandManager registerCommandGroup(IglooCommandGroup group) {
        commandGroups.add(group);
        linkCommandGroup(group);
        return this;
    }

    public CommandManager registerCommandGroup(Supplier<IglooCommandGroup> group) {
        return registerCommandGroup(group.get());
    }

    public boolean hasCommand(String cmd) {
        final String commandName = cmd.replace("/", "");
        return commands.stream().anyMatch(command -> command.getCommand().equals(commandName) || command.getCommandAliases().contains(commandName));// || commandGroups.stream().anyMatch(group -> group.getGroupCommand().equals(cmd) || group.getGroupCommandAliases().contains(cmd));
    }

    public Optional<IglooCommand> getCommand(String cmd) {
        final String commandName = cmd.replace("/", "");
        return commands.stream().filter(command -> command.getCommand().equals(commandName) || command.getCommandAliases().contains(commandName)).findFirst();
    }

    public boolean hasCommandGroup(String baseCommand) {
        final String commandName = baseCommand.replace("/", "");
        return commandGroups.stream().anyMatch(group -> group.getGroupCommand().equals(commandName) || group.getGroupCommandAliases().contains(commandName));
    }

    public Optional<IglooCommandGroup> getCommandGroup(String baseCommand) {
        final String commandName = baseCommand.replace("/", "");
        return commandGroups.stream().filter(command -> command.getGroupCommand().equals(commandName) || command.getGroupCommandAliases().contains(commandName)).findFirst();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasCommand(command.getName())) {
            IglooCommand subCommand = getCommand(command.getName()).get();
            return subCommand.onCommand(sender, command, label, ArrayUtil.copyArrayOfRange(args, args.length));
        } else if (hasCommandGroup(command.getName())) {
            IglooCommandGroup commandGroup = getCommandGroup(command.getName()).get();
            return commandGroup.onCommand(sender, command, label, args);
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
            if (!command.getCommandAliases().isEmpty()) {
                command.getCommandAliases().forEach(alias -> commandAliases.add("/" + alias));
            }
        });

        // TODO: check if args is greater than or 2 for cmd group?
        if (commandAliases.contains(args[0])) {
            String command = args[0].replace("/", "");
            if (hasCommand(command)) {
                getCommand(command).ifPresent(subCommand -> {
                    if (subCommand.hasPermission() && !permissionHandler.hasCommandPermission(player, subCommand.getPermission())) {
                        permissionHandler.onPermissionFailure(player, subCommand);
                        event.setCancelled(true);
                    }
                });
            } else if (hasCommandGroup(args[0])) {
                Logger.severe("cmd group");
            }
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && enabled) {
            if (hasCommand(label)) {
                IglooCommand subCommand = getCommand(label).get();
                if (subCommand.isEnabled() && permissionHandler.hasCommandPermission(sender, subCommand.getPermission())) {
                    return subCommand.onTabComplete(sender, command, label, args);
                }
            } else if (hasCommandGroup(label)) {
                IglooCommandGroup commandGroup = getCommandGroup(label).get();
                if (commandGroup.isEnabled()) {
                    return commandGroup.onTabComplete(sender, command, label, args);
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
                        if (cmd.startsWith(label) && !tab.contains(cmd) && permissionHandler.hasCommandPermission(sender, subCommand.getPermission())) {
                            tab.add(cmd);
                        }
                    }
                }
                for (IglooCommandGroup commandGroup : commandGroups) {
                    if (!commandGroup.isEnabled()) {
                        continue;
                    }

                    List<String> cmdAccess = new ArrayList<>(commandGroup.getGroupCommandAliases());
                    cmdAccess.add(commandGroup.getGroupCommand());

                    for (String cmd : cmdAccess) {
                        if (cmd.startsWith(label) && !tab.contains(cmd)) {
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
