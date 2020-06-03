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

    public static void handleHelp(Player player, String cmd) {
        if (cmd.isEmpty()) {
            String header = "&7-- &bCommands &7--";
            Helper.sendMessage(player, header);
            Helper.sendMessage(player, "&7» Add Member: &b/f add <player>");
            Helper.sendMessage(player, "&7» Demote Member: &b/f demote <player>");
            Helper.sendMessage(player, "&7» Help: &b/f help [command]");
            Helper.sendMessage(player, "&7» Info: &b/f info [player|farm]");
            Helper.sendMessage(player, "&7» Kick Member: &b/f kick <player>");
            Helper.sendMessage(player, "&7» Leave: &b/f leave");
            Helper.sendMessage(player, "&7» Promote Member: &b/f promote <player>");
            Helper.sendMessage(player, "&7» Rename Farm: &b/f rename <name>");
            Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replaceAll("&7", "").replaceAll("&b", "").length(), "-")));
        } else {
            switch (cmd.toLowerCase()) {
                case "add": {
                    String header = "&7-- &bCommand: Add &7--";
                    Helper.sendMessage(player, header);
                    Helper.sendMessage(player, "&7» Usage: &b/f add <player>");
                    Helper.sendMessage(player, "&7» Description: &bInvites the player to your farm");
                    Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replaceAll("&7", "").replaceAll("&b", "").length(), "-")));
                }

                case "demote": {
                    String header = "&7-- &bCommand: Demote &7--";
                    Helper.sendMessage(player, header);
                    Helper.sendMessage(player, "&7» Usage: &b/f demote <player>");
                    Helper.sendMessage(player, "&7» Description: &bDemotes the player to 1 lower rank in your farm");
                    Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replaceAll("&7", "").replaceAll("&b", "").length(), "-")));
                }

                case "kick": {
                    String header = "&7-- &bCommand: Kick &7--";
                    Helper.sendMessage(player, header);
                    Helper.sendMessage(player, "&7» Usage: &b/f kick <player>");
                    Helper.sendMessage(player, "&7» Description: &bKicks the player from your farm");
                    Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replaceAll("&7", "").replaceAll("&b", "").length(), "-")));
                }

                case "leave": {
                    String header = "&7-- &bCommand: Leave &7--";
                    Helper.sendMessage(player, header);
                    Helper.sendMessage(player, "&7» Usage: &b/f leave");
                    Helper.sendMessage(player, "&7» Description: &bLeave the current farm you're in");
                    Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replaceAll("&7", "").replaceAll("&b", "").length(), "-")));
                }

                case "promote": {
                    String header = "&7-- &bCommand: Promote &7--";
                    Helper.sendMessage(player, header);
                    Helper.sendMessage(player, "&7» Usage: &b/f promote <player>");
                    Helper.sendMessage(player, "&7» Description: &bPromotes the player 1 rank in your farm");
                    Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replaceAll("&7", "").replaceAll("&b", "").length(), "-")));
                }

                case "rename": {
                    String header = "&7-- &bCommand: Rename &7--";
                    Helper.sendMessage(player, header);
                    Helper.sendMessage(player, "&7» Usage: &b/f rename <name>");
                    Helper.sendMessage(player, "&7» Description: &bRenames your farm");
                    Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replaceAll("&7", "").replaceAll("&b", "").length(), "-")));
                }
            }
        }
    }

    // TODO: Make it use `target`
    public static void handleInfo(Player player, String target) {
        PlayerData playerData = FarmingPlugin.instance.playersCache.get(player.getUniqueId());

        if (checkSenderInFarm(player, playerData)) return;

        FarmData farmData = FarmingPlugin.instance.farmsCache.get(playerData.farmID);

        String header = String.format("&7-- &bFarm: &b&l%s &7--", farmData.name);
        Helper.sendMessage(player, header);
        Helper.sendMessage(player, "&7» Name: &b" + farmData.name);
        Helper.sendMessage(player, "&7» Owner: &b" + Bukkit.getOfflinePlayer(farmData.owner).getName());
        Helper.sendMessage(player, "&7» Area: &b" + farmData.regionInstance.getArea());
        Helper.sendMessage(player, "&7» Date of creation: &b" + farmData.regionInstance.getDate());
        Helper.sendMessage(player, "&7" + String.join("", Collections.nCopies(header.replaceAll("&7", "").replaceAll("&b", "").length(), "-")));
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

    public static void handleRename(Player player, String name) {
        if (checkPlayerFarmOwner(player)) return;

        FarmData farm = getPlayerFarm(player);
        farm.name = name;
        FarmingPlugin.instance.farmsCache.update(getPlayer(player).farmID, farm);
    }

    private static PlayerData getPlayer(Player player) {
        return FarmingPlugin.instance.playersCache.get(player.getUniqueId());
    }

    private static FarmData getPlayerFarm(Player player) {
        PlayerData data = FarmingPlugin.instance.playersCache.get(player.getUniqueId());
        if (checkSenderInFarm(player, data)) return null;

        return FarmingPlugin.instance.farmsCache.get(data.farmID);
    }

    private static boolean checkSenderInFarm(Player player, PlayerData sender) {
        if (sender.farmID == null) {
            Helper.sendMessage(player, String.format(Messages.ERROR_YOU_NOT_IN_FARM, sender.name));
            return true;
        }

        return false;
    }

    private static boolean checkPlayerFarmOwner(Player player) {
        FarmData farm = getPlayerFarm(player);
        if (farm != null) {
            if (getPlayer(player).permissionLevel == FarmPermissionLevel.LEADER) {
                return false;
            } else {
                Helper.sendMessage(player, Messages.ERROR_NOT_LEADER);
                return true;
            }
        }

        return true;
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
