package net.mistwood.FarmingPlugin.Modules.Shop;

import net.mistwood.FarmingPlugin.Main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ShopEvents implements Listener
{

    private static Main Instance;

    public ShopEvents (Main Instance)
    {
        this.Instance = Instance;

        Bukkit.getServer ().getPluginManager ().registerEvents (this, Instance);
    }

}
