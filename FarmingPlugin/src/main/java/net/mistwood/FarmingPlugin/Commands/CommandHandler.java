package net.mistwood.FarmingPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.mistwood.FarmingPlugin.FarmingPlugin;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;

import java.lang.reflect.Array;
import java.util.*;

public class CommandHandler implements CommandExecutor, TabCompleter, Listener {

    private final String cmd;
    private final List<String> aliases;
    private final Map<List<String>, SubCommand> commands = new HashMap<>();
    // TODO: Make a confirm system
    private final Map<String, String> commandConfirm = new HashMap<>();

    public CommandHandler(String cmd) {
        this(cmd, Collections.emptyList());
    }

    public CommandHandler(String cmd, List<String> aliases) {
        this.cmd = cmd;
        this.aliases = aliases;

        Objects.requireNonNull(FarmingPlugin.instance.getCommand(cmd)).setExecutor(this);
        Objects.requireNonNull(FarmingPlugin.instance.getCommand(cmd)).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0 && hasCommand(args[0])) {
            CommandExecutor Executor = getCommandSubCommand(args[0]);
            return Executor.onCommand(sender, cmd, label, copyArrayOfRange(args, args.length));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0 && hasCommand(args[0])) {
                TabCompleter completer = getCommandSubCommand(args[0]);
                return completer.onTabComplete(sender, cmd, alias, copyArrayOfRange(args, args.length));
            } else {
                SortedSet<String> tab = new TreeSet<>();
                for (List<String> commands : commands.keySet()) {
                    String key = commands.get(0);
                    if (key.startsWith(args[0]) && !tab.contains(key) && FarmingPlugin.instance.permissionManager.hasCommandPermission(sender, key))
                        tab.add(key);
                }

                return new ArrayList<>(tab);
            }
        } else {
            // TODO: Console commands?
            return new ArrayList<>();
        }
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        List<String> commandAliases = new ArrayList<>();
        aliases.forEach(s -> commandAliases.add("/" + s));

        if (args.length >= 2 && (args[0].equals("/" + cmd) || commandAliases.contains(args[0]))) {
            if (hasCommand(args[1]) && !FarmingPlugin.instance.permissionManager.hasCommandPermission(event.getPlayer(), args[1])) {
                Helper.sendMessage(player, Messages.ERROR_NO_COMMAND_PERMISSION);
                event.setCancelled(true);
            }
        }
    }

    public void registerCommand(List<String> cmd, SubCommand executor) {
        commands.put(cmd, executor);
    }

    private SubCommand getCommandSubCommand(String cmd) {
        return commands.entrySet().stream().filter(command -> command.getKey().contains(cmd)).findFirst().get().getValue();
    }

    private boolean hasCommand(String cmd) {
        return commands.keySet().stream().anyMatch(command -> command.contains(cmd));
    }

    private static <T> T[] copyArrayOfRange(T[] original, int end) {
        int start = 1;
        if (original.length >= start) {
            if (start <= end) {
                int length = end - start;
                int copyLength = Math.min(length, original.length - start);
                T[] Copy = (T[]) Array.newInstance(original.getClass().getComponentType(), length);

                System.arraycopy(original, start, Copy, 0, copyLength);

                return Copy;
            }

            throw new IllegalArgumentException();
        }

        throw new ArrayIndexOutOfBoundsException();
    }

}
