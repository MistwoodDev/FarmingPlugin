package net.mistwood.FarmingPlugin;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Database.DatabaseCollection;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import br.net.fabiozumbi12.RedProtect.Bukkit.API.events.CreateRegionEvent;

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
        if (!Event.getPlayer ().hasPlayedBefore () || Instance.Database.Exists (Event.getPlayer ().getUniqueId (), DatabaseCollection.PlayersCollection)) // Delete the database check once release
        {
            PlayerData Data = new PlayerData (Event.getPlayer (), Event.getPlayer ().getName (), null, null);

            Instance.PlayersCache.Add (Data.PlayerInstance.getUniqueId (), Data);

            Instance.Database.Insert (Data.ToMap (), DatabaseCollection.PlayersCollection);
        }

        else
        {
            PlayerData Data = PlayerData.FromMap (Instance.Database.Get (Event.getPlayer ().getUniqueId (), DatabaseCollection.PlayersCollection));

            // Add player to cache
            Instance.PlayersCache.Add (Data.PlayerInstance.getUniqueId (), Data);
            // Add players farm to cache (if the farm isn't already in the cache)
            Instance.FarmsCache.Add (Data.FarmID, FarmData.FromMap (Instance.Database.Get (Data.FarmID, DatabaseCollection.FarmsCollection))); // TODO: Maybe to check first?
            // Add the player to the cached farms `OnlinePlayers` list
            Instance.FarmsCache.Update (Data.FarmID, Instance.FarmsCache.Get (Data.FarmID).AddOnlinePlayer (Data));
        }
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent Event)
    {
        PlayerData Data = PlayerData.FromMap (Instance.Database.Get (Event.getPlayer ().getUniqueId (), DatabaseCollection.PlayersCollection));

        // Remove the player from the cache
        Instance.PlayersCache.Remove (Event.getPlayer ().getUniqueId ());
        // Remove the player from the cached farms `OnlinePlayers` list
        Instance.FarmsCache.Update (Data.FarmID, Instance.FarmsCache.Get (Data.FarmID).RemoveOnlinePlayer (Data));
        // Remove the cached farm if the player is the last one online
        if (Instance.FarmsCache.Get (Data.FarmID).OnlinePlayers.size () < 1)
        {
            FarmData Farm = Instance.FarmsCache.Get (Data.FarmID);
            Instance.FarmsCache.Remove (Data.FarmID);

            Instance.Database.Update (Farm.ID, Farm.ToMap (), DatabaseCollection.FarmsCollection);
        }
        // Add the player and farm (only if needed) to the database
        Instance.Database.Update (Data.PlayerInstance.getUniqueId (), Data.ToMap (), DatabaseCollection.PlayersCollection);
    }

}
