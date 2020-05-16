package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.FarmingPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoteMemberCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0 && (sender instanceof Player && FarmingPlugin.instance.permissionManager.hasCommandPermission(sender, "demote"))) {
            Player player = (Player) sender;

            CommandHelper.handleDemoteMember(player, args[0]);

            return true;
        }

        // TODO: Send command help message
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = FarmingPlugin.instance.playersCache.get(player.getUniqueId());
        if (playerData.farmID == null)
            return new ArrayList<>();

        FarmData farmData = FarmingPlugin.instance.farmsCache.get(playerData.farmID);
        List<PlayerData> onlinePlayers = farmData.onlinePlayers;
        onlinePlayers.remove(playerData);

        if (args.length == 1)
            if (args[0].isEmpty())
                return onlinePlayers.stream().map(PlayerData::getName).collect(Collectors.toList());
            else
                return onlinePlayers.stream().filter(data -> data.name.toLowerCase().startsWith(args[0].toLowerCase())).map(PlayerData::getName).collect(Collectors.toList());

        return new ArrayList<>();
    }

}
