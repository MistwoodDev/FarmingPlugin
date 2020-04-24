package net.mistwood.FarmingPlugin.Modules.Fishing;

import org.bukkit.event.enchantment.EnchantItemEvent;
import net.mistwood.FarmingPlugin.Main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DisableEnchanting implements Listener {

    private static Main Instance;

    public DisableEnchanting (Main Instance)
    {
        this.Instance = Instance;
    }
    
    @EventHandler
    public void onEnchant (EnchantItemEvent Event) {
    	ItemStack Rod = Event.getItem ();
    	
        if (Rod.getItemMeta ().hasLore ()) {
        	if (Rod.getItemMeta ().getLore ().get (0).startsWith ("Fishing rod - ")) {
            	Event.setCancelled (true);
        	}
        }
    }
}