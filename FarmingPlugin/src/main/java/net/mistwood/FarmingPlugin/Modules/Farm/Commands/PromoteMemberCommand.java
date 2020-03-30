package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PromoteMemberCommand implements SubCommand
{

    private Main Instance;

    public PromoteMemberCommand (Main Instance)
    {
        this.Instance = Instance;
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        if (Args.length == 1 && (Sender instanceof Player) && Instance.PermissionManager.HasCommandPermission (Sender, "promote"))
        {
            Player Target = (Player) Sender;

            CommandHelper.HandlePromoteMember (Instance, Target, Args[0]);

            return true;
        }

        // TODO: Send command help message
        return true;
    }

    @Override
    public List<String> onTabComplete (CommandSender Sender, Command Command, String Alias, String[] Args)
    {
        Player Target = (Player) Sender;
        PlayerData TargetPlayer = Instance.PlayersCache.Get (Target.getUniqueId ());
        if (TargetPlayer.FarmID == null)
            return new ArrayList<> ();

        FarmData Farm = Instance.FarmsCache.Get (TargetPlayer.FarmID);
        List<PlayerData> Targets = Farm.OnlinePlayers;
        Targets.remove (TargetPlayer);

        if (Args.length == 1)
            if (Args[0].isEmpty ())
                return Targets.stream ().map (PlayerData::GetName).collect (Collectors.toList ());
            else
                return Targets.stream ().filter (Player -> Player.Name.toLowerCase ().startsWith (Args[0].toLowerCase ())).map (PlayerData::GetName).collect (Collectors.toList ());

        return new ArrayList<> ();
    }

}
