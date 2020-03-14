package net.mistwood.FarmingPlugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface EventManager
{

    void OnInventoryClick (Inventory Inventory, int Slot, ClickType Click, ItemStack CurrentItem);
    void OnPlayerFish (Player Player, Entity Caught, int ExpToDrop, FishHook Hook);

}
