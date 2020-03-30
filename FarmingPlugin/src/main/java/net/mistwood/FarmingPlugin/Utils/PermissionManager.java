package net.mistwood.FarmingPlugin.Utils;

import net.mistwood.FarmingPlugin.Data.FarmPermissionLevel;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionManager
{

    private static final String BasePerm = "farming";
    private static final String CommandBasePerm = BasePerm + "." + "command";
    private static final String CommandBasePermAdmin = BasePerm + "." + "command.admin";

    public boolean HasCommandPermission (CommandSender Target, String Permission)
    {
        String AdminPermission = CommandBasePermAdmin + "." + Permission;
        String UserPermission = CommandBasePerm + "." + Permission;

        return HasPermission (Target, AdminPermission) || HasPermission (Target, UserPermission);
    }

    public boolean HasPermissionOrByPass (Player Target, String Permission)
    {
        return Target.hasPermission (Permission) || Target.hasPermission (Permission + ".bypass");
    }

    public boolean HasPermission (CommandSender Target, String Permission)
    {
        return !(Target instanceof Player) || HasPermission ((Player) Target, Permission);
    }

    public boolean HasPermission (Player Target, String Permission)
    {
        return Target != null && (Target.hasPermission (Permission) || Target.hasPermission (CommandBasePermAdmin));
    }

    public boolean HasFarmPermissionMember (Player Target, String Permission, PlayerData TargetPlayer)
    {
        return FarmPermissionMember (Target, Permission, TargetPlayer);
    }

    public boolean HasFarmPermissionAdmin (CommandSender Target, String Permission, PlayerData TargetPlayer)
    {
        return !(Target instanceof Player) || FarmPermissionAdmin ((Player) Target, Permission, TargetPlayer);
    }

    public boolean HasFarmPermissionAdmin (Player Target, String Permission, PlayerData TargetPlayer)
    {
        return FarmPermissionAdmin (Target, Permission, TargetPlayer);
    }

    public boolean HasFarmPermissionLeader (CommandSender Target, String Permission, PlayerData TargetPlayer)
    {
        return !(Target instanceof Player) || FarmPermissionLeader ((Player) Target, Permission, TargetPlayer);
    }

    public boolean HasFarmPermissionLeader (Player Target, String Permission, PlayerData TargetPlayer)
    {
        return FarmPermissionLeader (Target, Permission, TargetPlayer);
    }

    private boolean FarmPermissionMember (Player Target, String Permission, PlayerData TargetPlayer)
    {
        String AdminPermission = CommandBasePermAdmin + "." + Permission;
        String UserPermission = CommandBasePerm + "." + Permission;

        if (TargetPlayer == null)
            return HasPermission (Target, AdminPermission) || HasPermission (Target, UserPermission);

        return HasPermission (Target, AdminPermission) || (HasPermission (Target, UserPermission) && (TargetPlayer.PermissionLevel == FarmPermissionLevel.Leader || TargetPlayer.PermissionLevel == FarmPermissionLevel.Admin || TargetPlayer.PermissionLevel == FarmPermissionLevel.Farmer));
    }

    private boolean FarmPermissionAdmin (Player Target, String Permission, PlayerData TargetPlayer)
    {
        String AdminPermission = CommandBasePermAdmin + "." + Permission;
        String UserPermission = CommandBasePerm + "." + Permission;

        if (TargetPlayer == null)
            return HasPermission (Target, AdminPermission) || HasPermission (Target, UserPermission);

        return HasPermission (Target, AdminPermission) || (HasPermission (Target, UserPermission) && (TargetPlayer.PermissionLevel == FarmPermissionLevel.Leader || TargetPlayer.PermissionLevel == FarmPermissionLevel.Admin));
    }

    private boolean FarmPermissionLeader (Player Target, String Permission, PlayerData TargetPlayer)
    {
        String AdminPermission = CommandBasePermAdmin + "." + Permission;
        String UserPermission = CommandBasePerm + "." + Permission;

        if (TargetPlayer == null)
            return HasPermission (Target, AdminPermission) || HasPermission (Target, UserPermission);

        return HasPermission (Target, AdminPermission) || (HasPermission (Target, UserPermission) && TargetPlayer.PermissionLevel == FarmPermissionLevel.Leader);
    }

}
