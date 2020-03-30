package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.FarmPermissionLevel;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;

public class CommandHelper
{

    public static void HandleHelp (Main Instance, Player Sender, String Command)
    {

    }

    public static void HandleInfo (Main Instance, Player Sender)
    {
        PlayerData Player = Instance.PlayersCache.Get (Sender.getUniqueId ());

        if (!CheckSenderInFarm (Sender, Player)) return;

        FarmData Farm = Instance.FarmsCache.Get (Player.FarmID);

        String Header = String.format ("&7-- &bFarm: &b&l%s &7--", Farm.Name);
        Helper.SendMessage (Sender, Header);
        Helper.SendMessage (Sender, "&7» Name: &b" + Farm.Name);
        Helper.SendMessage (Sender, "&7» Owner: &b" + Bukkit.getOfflinePlayer (Farm.Owner).getName ());
        Helper.SendMessage (Sender, "&7» Area: &b" + Farm.RegionInstance.getArea ());
        Helper.SendMessage (Sender, "&7» Date of creation: &b" + Farm.RegionInstance.getDate ());
        Helper.SendMessage (Sender, "&7" + String.join ("", Collections.nCopies (Header.length (), "-")));
    }

    // TODO:
    public static void HandleAddMember (Main Instance, Player Sender, String TargetName)
    {
        PlayerData Player = Instance.PlayersCache.Get (Sender.getUniqueId ());

        if (!CheckSenderInFarm (Sender, Player)) return;

        if (!CheckFarmAdminPermission (Instance, Sender, Player, "add")) return;;

        Player TargetMember = Bukkit.getPlayer (TargetName);

        if (!CheckPlayerFound (TargetMember, Sender)) return;

        PlayerData TargetPlayer = Instance.PlayersCache.Get (TargetMember.getUniqueId ());

        if (!CheckPlayerFound (TargetPlayer, Sender)) return;

        if (!CheckPlayerInFarm (Sender, TargetPlayer)) return;

        FarmData Farm = Instance.FarmsCache.Get (Player.FarmID);
        Farm.AddPlayer (TargetMember.getUniqueId ());
        Farm.AddOnlinePlayer (TargetPlayer);
        TargetPlayer.FarmID = Farm.ID;
        TargetPlayer.FarmName = Farm.Name;
        TargetPlayer.PermissionLevel = FarmPermissionLevel.Farmer;

        Instance.FarmsCache.Update (Farm.ID, Farm);
        Instance.PlayersCache.Update (TargetMember.getUniqueId (), TargetPlayer);
    }

    public static void HandleKickMember (Main Instance, Player Sender, String TargetName)
    {

    }

    public static void HandlePromoteMember (Main Instance, Player Sender, String TargetName)
    {

    }

    public static void HandleDemoteMember (Main Instance, Player Sender, String TargetName)
    {

    }

    public static void HandleLeaveFarm (Main Instance, Player Sender)
    {

    }

    public static void HandleAccept (Main Instance, Player Sender)
    {

    }

    public static void HandleDeny (Main Instance, Player Sender)
    {

    }

    public static void HandleRename (Main Instance, Player Sender, String NewName)
    {

    }

    private static boolean CheckSenderInFarm (Player Target, PlayerData Sender)
    {
        if (Sender.FarmID == null)
        {
            Helper.SendMessage (Target, String.format (Messages.YouNotInFarm, Sender.Name));
            return false;
        }

        return true;
    }

    private static boolean CheckFarmAdminPermission (Main Instance, Player Target, PlayerData Sender, String Permission)
    {
        if (!Instance.PermissionManager.HasFarmPermissionAdmin (Target, Permission, Sender))
        {
            Helper.SendMessage (Target, Messages.NoCommandPermission);
            return false;
        }

        return true;
    }

    private static boolean CheckPlayerFound (Player Target, Player Sender)
    {
        if (Target == null)
        {
            Helper.SendMessage (Sender, Messages.PlayerNotFound);
            return false;
        }

        return true;
    }

    private static boolean CheckPlayerFound (PlayerData Target, Player Sender)
    {
        if (Target == null)
        {
            Helper.SendMessage (Sender, Messages.PlayerNotFound);
            return false;
        }

        return true;
    }

    private static boolean CheckPlayerInFarm (Player Target, PlayerData Sender)
    {
        if (Sender.FarmID != null)
        {
            Helper.SendMessage (Target, String.format (Messages.PlayerAlreadyInFarm, Sender.Name));
            return false;
        }

        return true;
    }

}
