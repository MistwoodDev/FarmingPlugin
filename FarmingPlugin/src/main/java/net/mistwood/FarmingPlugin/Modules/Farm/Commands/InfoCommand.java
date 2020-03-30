package net.mistwood.FarmingPlugin.Modules.Farm.Commands;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InfoCommand implements SubCommand
{

    private Main Instance;

    public InfoCommand (Main Instance)
    {
        this.Instance = Instance;
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        if (Sender instanceof Player && Instance.PermissionManager.HasCommandPermission (Sender, "info"))
        {
            Player Target = (Player) Sender;

            CommandHelper.HandleInfo (Instance, Target);

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
