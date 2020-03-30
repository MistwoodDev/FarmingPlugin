package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RenameCommand implements SubCommand
{

    private Main Instance;

    public RenameCommand (Main Instance)
    {
        this.Instance = Instance;
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        if (Args.length > 0 && (Sender instanceof Player && Instance.PermissionManager.HasCommandPermission (Sender, "rename")))
        {
            Player Target = (Player) Sender;

            CommandHelper.HandleRename (Instance, Target, Args[0]);

            return true;
        }

        // TODO: Send command help message
        return true;
    }

    @Override
    public List<String> onTabComplete (CommandSender Sender, Command Command, String Alias, String[] Args)
    {
        return new ArrayList<> ();
    }

}
