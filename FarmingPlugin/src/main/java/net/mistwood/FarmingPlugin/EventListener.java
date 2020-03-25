package net.mistwood.FarmingPlugin;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Database.DatabaseCollection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        Player Target = Event.getPlayer ();

        Instance.Database.Exists (Target.getUniqueId (), DatabaseCollection.PlayersCollection, (Count, Throwable) -> {
            boolean Exists = Count > 0;

            if (!Target.hasPlayedBefore () || !Exists)
            {
                PlayerData Data = new PlayerData (Target, Target.getName (),null, null,null);

                Instance.PlayersCache.Add (Target.getUniqueId (), Data);

                // TODO: Do we actually wanna insert the player now? Or wait till he leaves?
                Instance.Database.Insert (Data.ToMap (), DatabaseCollection.PlayersCollection);
            }

            else
            {
                Instance.Database.Get (Target.getUniqueId (), DatabaseCollection.PlayersCollection, (Result, Error) -> {
                    PlayerData Data = PlayerData.FromMap (Result);

                    // Add player to cache
                    Instance.PlayersCache.Add (Target.getUniqueId (), Data);

                    // Is the player in a farm?
                    if (Data.FarmID != null)
                    {
                        Instance.Database.Get (Data.FarmID, DatabaseCollection.FarmsCollection, (FarmResult, FarmError) -> {
                            // Add players farm to cache (if the farm isn't already in the cache)
                            Instance.FarmsCache.Add (Data.FarmID, FarmData.FromMap (FarmResult)); // TODO: Maybe to check first?
                            // Add the player to the cached farms `OnlinePlayers` list
                            Instance.FarmsCache.Update (Data.FarmID, Instance.FarmsCache.Get (Data.FarmID).AddOnlinePlayer (Data));
                        });
                    }
                });
            }
        });
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent Event)
    {
        Player Target = Event.getPlayer ();

        PlayerData Data = Instance.PlayersCache.Get (Target.getUniqueId ());

        // Remove the player from the cache
        Instance.PlayersCache.Remove (Target.getUniqueId ());

        // Is the player in a farm?
        if (Data.FarmID != null)
        {
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
            Instance.Database.Update (Target.getUniqueId (), Data.ToMap (), DatabaseCollection.PlayersCollection);
        }
    }

}
