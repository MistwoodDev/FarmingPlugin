package net.mistwood.FarmingPlugin.Modules.Fishing.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Commands.SubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            Player player = (Player) sender;
            CommandHelper.handleHelp(player, args);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1)
            if (args[0].isEmpty())
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            else
                return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(args[0].toLowerCase())).map(Player::getName).collect(Collectors.toList());

        return new ArrayList<>();
    }
}
