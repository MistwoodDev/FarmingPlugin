package net.mistwood.FarmingPlugin.Modules.DiscordLink.Commands;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LinkCommand implements SubCommand
{

    private static Main Instance;

    public LinkCommand (Main Instance)
    {
        this.Instance = Instance;
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        if (!(Sender instanceof Player))
        {
            Helper.SendMessage (Sender, "You must be a player to perform this command!");
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        return new ArrayList<> ();
    }
}
