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

public class AddMemberCommand implements SubCommand
{

    private Main Instance;

    public AddMemberCommand (Main Instance)
    {
        this.Instance = Instance;
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        // TODO: Check perms
        if (Args.length == 1 && (Sender instanceof Player))
        {
            Player Target = (Player) Sender;
            CommandHelper.HandleAddMember (Instance, Target, Args[0]);

            return true;
        }

        // TODO: Send command help message
        return true;
    }

    @Override
    public List<String> onTabComplete (CommandSender Sender, Command Command, String Alias, String[] Args)
    {
        if (Args.length == 1)
            if (Args[0].isEmpty ())
                return Bukkit.getOnlinePlayers ().stream ().map (Player::getName).collect (Collectors.toList ());
            else
                return Bukkit.getOnlinePlayers ().stream ().filter (Player -> Player.getName ().toLowerCase ().startsWith (Args[0].toLowerCase ())).map (Player::getName).collect (Collectors.toList ());

        return new ArrayList<> ();
    }

}
