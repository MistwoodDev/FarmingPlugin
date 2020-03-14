package net.mistwood.FarmingPlugin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener
{

    private static Main Instance;

    public EventListener (Main Instance)
    {
        this.Instance = Instance;

        Bukkit.getServer ().getPluginManager ().registerEvents (this, Instance);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent Event)
    {
        // Add player to cache
        // Add players farm to cache (if the farm isn't already in the cache)
        // Add the player to the cached farms `OnlinePlayers` list
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent Event)
    {
        // Remove the player from the cache
        // Remove the player from the cached farms `OnlinePlayers` list
        // Remove the cached farm if the player is the last one online
        // Add the player and farm (only if needed) to the database
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent Event)
    {
        for (EventManager Manager : Instance.EventManagers)
            Manager.OnInventoryClick (Event.getInventory (), Event.getSlot (), Event.getClick (), Event.getCurrentItem ());
    }

    @EventHandler
    public void onPlayerFish (PlayerFishEvent Event)
    {
        for (EventManager Manager : Instance.EventManagers)
            Manager.OnPlayerFish (Event.getPlayer (), Event.getCaught (), Event.getExpToDrop (), Event.getHook ());
    }

    // TODO: Add more events

}
