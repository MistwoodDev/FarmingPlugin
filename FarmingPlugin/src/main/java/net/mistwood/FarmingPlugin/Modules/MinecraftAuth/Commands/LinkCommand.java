package net.mistwood.FarmingPlugin.Modules.MinecraftAuth.Commands;

import net.mistwood.FarmingPlugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor
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
            return false;

        return true;
    }

}
