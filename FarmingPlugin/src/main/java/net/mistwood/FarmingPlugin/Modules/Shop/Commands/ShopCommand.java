package net.mistwood.FarmingPlugin.Modules.Shop.Commands;

import net.mistwood.FarmingPlugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShopCommand implements CommandExecutor
{

    private static Main Instance;

    public ShopCommand (Main Instance)
    {
        this.Instance = Instance;
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        return true;
    }

}
