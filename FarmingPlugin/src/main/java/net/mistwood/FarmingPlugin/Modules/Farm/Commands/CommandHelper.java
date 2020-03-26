package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.FarmPermissionLevel;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandHelper
{

    public static void HandleAddMember (Main Instance, Player Owner, String TargetName)
    {
        PlayerData Player = Instance.PlayersCache.Get (Owner.getUniqueId ());

        if (Player.FarmID == null)
        {
            Helper.SendMessage (Owner, Messages.PlayerNotInFarm);
            return;
        }

        Player TargetMember = Bukkit.getPlayer (TargetName);

        if (TargetMember == null)
        {
            Helper.SendMessage (Owner, Messages.PlayerNotFound);
            return;
        }

        FarmData Farm = Instance.FarmsCache.Get (Player.FarmID);
        PlayerData TargetPlayer = Instance.PlayersCache.Get (TargetMember.getUniqueId ());

        Farm.AddPlayer (TargetMember.getUniqueId ());
        Farm.AddOnlinePlayer (TargetPlayer);
        TargetPlayer.FarmID = Farm.ID;
        TargetPlayer.FarmName = Farm.Name;
        TargetPlayer.PermissionLevel = FarmPermissionLevel.Farmer;

        Instance.FarmsCache.Update (Farm.ID, Farm);
        Instance.PlayersCache.Update (TargetMember.getUniqueId (), TargetPlayer);
    }

}
