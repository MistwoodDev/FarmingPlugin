package net.mistwood.FarmingPlugin.Modules.Shop;

import net.mistwood.FarmingPlugin.EventManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopEvents implements EventManager
{

    @Override
    public void OnInventoryClick (Inventory Inventory, int Slot, ClickType Click, ItemStack CurrentItem)
    {
        // example
    }

    @Override
    public void OnPlayerFish (Player Player, Entity Caught, int ExpToDrop, FishHook Hook)
    {
        // example
    }
}
