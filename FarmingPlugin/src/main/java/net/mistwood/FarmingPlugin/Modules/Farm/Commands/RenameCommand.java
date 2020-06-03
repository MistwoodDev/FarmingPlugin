package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.FarmingPlugin;

import java.util.ArrayList;
import java.util.List;

public class RenameCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && FarmingPlugin.instance.permissionManager.hasCommandPermission(sender, "rename")) {
            Player player = (Player) sender;

            if (args.length == 1) {
                CommandHelper.handleRename(player, args[0]);
            } else {
                CommandHelper.handleHelp(player, "rename");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return new ArrayList<>();
    }

}
