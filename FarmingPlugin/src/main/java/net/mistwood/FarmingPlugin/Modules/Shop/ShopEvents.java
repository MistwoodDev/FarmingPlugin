package net.mistwood.FarmingPlugin.Modules.Shop;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import net.mistwood.FarmingPlugin.FarmingPlugin;

public class ShopEvents implements Listener {

    public ShopEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, FarmingPlugin.instance);
    }

}
