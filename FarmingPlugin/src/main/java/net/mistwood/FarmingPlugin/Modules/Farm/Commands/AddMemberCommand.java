package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.FarmingPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddMemberCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && FarmingPlugin.instance.permissionManager.hasCommandPermission(sender, "add")) {
            Player player = (Player) sender;

            if (args.length == 1) {
                CommandHelper.handleAddMember(player, args[0]);
            } else {
                CommandHelper.handleHelp(player, "add");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            if (args[0].isEmpty())
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            else
                return FarmingPlugin.instance.modules.stream().filter(module -> module.getName().toLowerCase().startsWith(args[0].toLowerCase())).map(Module::getName).collect(Collectors.toList());

        return new ArrayList<>();
    }

}
