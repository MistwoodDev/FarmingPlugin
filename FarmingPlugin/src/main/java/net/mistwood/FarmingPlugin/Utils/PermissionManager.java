package net.mistwood.FarmingPlugin.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Data.FarmPermissionLevel;
import net.mistwood.FarmingPlugin.Data.PlayerData;

public class PermissionManager {

    private static final String BASE_PERM = "farming";
    private static final String COMMAND_BASE_PERM = BASE_PERM + "." + "command";
    private static final String COMMAND_BASE_PERM_ADMIN = BASE_PERM + "." + "command.admin";

    public boolean hasCommandPermission(CommandSender sender, String perm) {
        String AdminPermission = COMMAND_BASE_PERM_ADMIN + "." + perm;
        String UserPermission = COMMAND_BASE_PERM + "." + perm;

        return hasPermission(sender, AdminPermission) || hasPermission(sender, UserPermission);
    }

    public boolean hasPermissionOrByPass(Player player, String perm) {
        return player.hasPermission(perm) || player.hasPermission(perm + ".bypass");
    }

    public boolean hasPermission(CommandSender sender, String perm) {
        return !(sender instanceof Player) || hasPermission((Player) sender, perm);
    }

    public boolean hasPermission(Player player, String perm) {
        return player != null && (player.hasPermission(perm) || player.hasPermission(COMMAND_BASE_PERM_ADMIN));
    }

    public boolean hasFarmPermissionMember(Player player, String perm, PlayerData data) {
        return farmPermissionMember(player, perm, data);
    }

    public boolean hasFarmPermissionAdmin(CommandSender sender, String perm, PlayerData data) {
        return !(sender instanceof Player) || farmPermissionAdmin((Player) sender, perm, data);
    }

    public boolean hasFarmPermissionAdmin(Player player, String perm, PlayerData data) {
        return farmPermissionAdmin(player, perm, data);
    }

    public boolean hasFarmPermissionLeader(CommandSender sender, String perm, PlayerData data) {
        return !(sender instanceof Player) || farmPermissionLeader((Player) sender, perm, data);
    }

    public boolean hasFarmPermissionLeader(Player player, String perm, PlayerData data) {
        return farmPermissionLeader(player, perm, data);
    }

    private boolean farmPermissionMember(Player player, String perm, PlayerData data) {
        String admin = COMMAND_BASE_PERM_ADMIN + "." + perm;
        String user = COMMAND_BASE_PERM + "." + perm;

        if (data == null)
            return hasPermission(player, admin) || hasPermission(player, user);

        return hasPermission(player, admin) || (hasPermission(player, user) && (data.permissionLevel == FarmPermissionLevel.LEADER || data.permissionLevel == FarmPermissionLevel.ADMIN || data.permissionLevel == FarmPermissionLevel.FARMER));
    }

    private boolean farmPermissionAdmin(Player player, String perm, PlayerData data) {
        String admin = COMMAND_BASE_PERM_ADMIN + "." + perm;
        String user = COMMAND_BASE_PERM + "." + perm;

        if (data == null)
            return hasPermission(player, admin) || hasPermission(player, user);

        return hasPermission(player, admin) || (hasPermission(player, user) && (data.permissionLevel == FarmPermissionLevel.LEADER || data.permissionLevel == FarmPermissionLevel.ADMIN));
    }

    private boolean farmPermissionLeader(Player player, String perm, PlayerData data) {
        String admin = COMMAND_BASE_PERM_ADMIN + "." + perm;
        String user = COMMAND_BASE_PERM + "." + perm;

        if (data == null)
            return hasPermission(player, admin) || hasPermission(player, user);

        return hasPermission(player, admin) || (hasPermission(player, user) && data.permissionLevel == FarmPermissionLevel.LEADER);
    }

}
