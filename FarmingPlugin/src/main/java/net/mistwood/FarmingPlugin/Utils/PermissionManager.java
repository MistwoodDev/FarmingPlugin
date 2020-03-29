package net.mistwood.FarmingPlugin.Utils;

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

}
