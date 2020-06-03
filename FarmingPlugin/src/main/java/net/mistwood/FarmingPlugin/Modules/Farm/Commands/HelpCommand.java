package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Commands.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0) {
                CommandHelper.handleHelp(player, args[0]);
            } else {
                CommandHelper.handleHelp(player, "");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        // TODO: Return a list of registered command names
        return new ArrayList<>();
    }

}
