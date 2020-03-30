package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements SubCommand
{

    private Main Instance;

    public HelpCommand (Main Instance)
    {
        this.Instance = Instance;
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        if (Sender instanceof Player && Instance.PermissionManager.HasCommandPermission (Sender, "help"))
        {
            Player Target = (Player) Sender;

            if (Args.length > 0)
                CommandHelper.HandleHelp (Instance, Target, Args[0]);
            else
                CommandHelper.HandleHelp (Instance, Target, "");

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
