package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.FarmPermissionLevel;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.FarmingPlugin;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;

import java.util.Collections;

public class CommandHelper {

    public static void handleHelp(Player player, String cmd) { }

    public static void handleInfo(Player player) {
        PlayerData playerData = FarmingPlugin.instance.playersCache.get(player.getUniqueId());

        if (checkSenderInFarm(player, playerData)) return;

        FarmData farmData = FarmingPlugin.instance.farmsCache.get(playerData.farmID);

        String header = String.format("&7-- &bFarm: &b&l%s &7--", farmData.name);
        Helper.sendMessage(player, header);
        Helper.sendMessage(player, "&7» Name: &b" + farmData.name);
        Helper.sendMessage(player, "&7» Owner: &b" + Bukkit.getOfflinePlayer(farmData.owner).getName());
        Helper.sendMessage(player, "&7» Area: &b" + farmData.regionInstance.getArea());
        Helper.sendMessage(player, "&7» Date of creation: &b" + farmData.regionInstance.getDate());
        Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replace("&7", "").replace("&b", "").length(), "-")));
    }

    public static void handleAddMember(Player player, String name) {
        PlayerData playerData = FarmingPlugin.instance.playersCache.get(player.getUniqueId());

        if (checkSenderInFarm(player, playerData)) return;
        if (!checkFarmAdminPermission(player, playerData, "add")) return;

        Player member = Bukkit.getPlayer(name);
        if (!checkPlayerFound(member, player)) return;

        assert member != null;
        PlayerData targetPlayer = FarmingPlugin.instance.playersCache.get(member.getUniqueId());

        if (!checkPlayerFound(targetPlayer, player)) return;
        if (!checkPlayerInFarm(player, targetPlayer)) return;

        FarmData farmData = FarmingPlugin.instance.farmsCache.get(playerData.farmID);
        farmData.addPlayer(member.getUniqueId());
        farmData.addOnlinePlayer(targetPlayer);
        targetPlayer.farmID = farmData.id;
        targetPlayer.farmName = farmData.name;
        targetPlayer.permissionLevel = FarmPermissionLevel.FARMER;

        FarmingPlugin.instance.farmsCache.update(farmData.id, farmData);
        FarmingPlugin.instance.playersCache.update(member.getUniqueId(), targetPlayer);
    }

    public static void handleKickMember(Player player, String name) { }

    public static void handlePromoteMember(Player player, String name) { }

    public static void handleDemoteMember(Player player, String name) { }

    public static void handleLeaveFarm(Player player) { }

    public static void handleAccept(Player player) { }

    public static void handleDeny(Player player) { }

    public static void handleRename(Player player, String name) { }

    private static boolean checkSenderInFarm(Player player, PlayerData sender) {
        if (sender.farmID == null) {
            Helper.sendMessage(player, String.format(Messages.ERROR_YOU_NOT_IN_FARM, sender.name));
            return true;
        }

        return false;
    }

    private static boolean checkFarmAdminPermission(Player player, PlayerData sender, String perm) {
        if (!FarmingPlugin.instance.permissionManager.hasFarmPermissionAdmin(player, perm, sender)) {
            Helper.sendMessage(player, Messages.ERROR_NO_COMMAND_PERMISSION);
            return false;
        }

        return true;
    }

    private static boolean checkPlayerFound(Player player, Player sender) {
        if (player == null) {
            Helper.sendMessage(sender, Messages.ERROR_PLAYER_NOT_FOUND);
            return false;
        }

        return true;
    }

    private static boolean checkPlayerFound(PlayerData playerData, Player sender) {
        if (playerData == null) {
            Helper.sendMessage(sender, Messages.ERROR_PLAYER_NOT_FOUND);
            return false;
        }

        return true;
    }

    private static boolean checkPlayerInFarm(Player player, PlayerData sender) {
        if (sender.farmID != null) {
            Helper.sendMessage(player, String.format(Messages.ERROR_PLAYER_ALREADY_IN_FARM, sender.name));
            return false;
        }

        return true;
    }

}
