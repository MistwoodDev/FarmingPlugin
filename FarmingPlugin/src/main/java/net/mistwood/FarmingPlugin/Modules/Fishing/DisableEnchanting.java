package net.mistwood.FarmingPlugin.Modules.Fishing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import org.bukkit.event.enchantment.EnchantItemEvent;

public class DisableEnchanting implements Listener {

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        ItemStack rod = event.getItem();

        if (rod.getItemMeta().hasLore()) {
            if (rod.getItemMeta().getLore().get(0).startsWith("Fishing rod - ")) {
                event.setCancelled(true);
            }
        }
    }
}