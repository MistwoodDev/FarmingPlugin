package net.mistwood.FarmingPlugin.Modules.Shop;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Shop.Commands.ShopCommand;
import org.bukkit.Bukkit;

public class ShopModule implements Module
{

    private static Main Instance;

    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        Instance.RegisterEventManager (new ShopEvents ());
        Instance.RegisterCommand ("shop", new ShopCommand (Instance));
    }

    public void OnDisable ()
    {

    }

}
