package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.FarmingPlugin;

import java.util.ArrayList;
import java.util.List;

public class LeaveCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && FarmingPlugin.instance.permissionManager.hasCommandPermission(sender, "leave")) {
            Player player = (Player) sender;
            CommandHelper.handleLeaveFarm(player);

            return true;
        }

        // TODO: Send command help message
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return new ArrayList<>();
    }

}
