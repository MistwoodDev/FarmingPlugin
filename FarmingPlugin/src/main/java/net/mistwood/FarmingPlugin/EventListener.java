package net.mistwood.FarmingPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Database.DatabaseCollection;

public class EventListener implements Listener {

    public EventListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, FarmingPlugin.instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        FarmingPlugin.instance.database.exists(player.getUniqueId(), DatabaseCollection.PLAYERS, (count, t) -> {
            boolean exists = count > 0;

            if (!player.hasPlayedBefore() || !exists) {
                PlayerData playerData = new PlayerData(player, player.getName(), null, null, null);
                FarmingPlugin.instance.playersCache.add(player.getUniqueId(), playerData);

                // TODO: Do we actually wanna insert the player now? Or wait till he leaves?
                FarmingPlugin.instance.database.insert(playerData.toMap(), DatabaseCollection.PLAYERS);
            } else {
                FarmingPlugin.instance.database.get(player.getUniqueId(), DatabaseCollection.PLAYERS, (Result, Error) -> {
                    PlayerData playerData = PlayerData.fromMap(Result);
                    FarmingPlugin.instance.playersCache.add(player.getUniqueId(), playerData);

                    if (playerData.farmID != null) {
                        FarmingPlugin.instance.database.exists(playerData.farmID, DatabaseCollection.FARMS, (FarmCount, FError) -> {
                            if (FarmCount > 0) {
                                FarmingPlugin.instance.database.get(playerData.farmID, DatabaseCollection.FARMS, (FarmResult, FarmError) -> {
                                    FarmingPlugin.instance.farmsCache.add(playerData.farmID, FarmData.fromMap(FarmResult)); // TODO: Maybe to check first?
                                    FarmingPlugin.instance.farmsCache.update(playerData.farmID, FarmingPlugin.instance.farmsCache.get(playerData.farmID).addOnlinePlayer(playerData));
                                });
                            } else {
                                PlayerData data = FarmingPlugin.instance.playersCache.get(playerData.playerInstance.getUniqueId());
                                data.farmID = null;
                                data.farmName = null;
                                data.permissionLevel = null;
                                FarmingPlugin.instance.playersCache.update(data.playerInstance.getUniqueId(), data);
                            }
                        });
                    }
                });
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = FarmingPlugin.instance.playersCache.get(player.getUniqueId());
        FarmingPlugin.instance.playersCache.remove(player.getUniqueId());

        if (playerData.farmID != null) {
            FarmingPlugin.instance.farmsCache.update(playerData.farmID, FarmingPlugin.instance.farmsCache.get(playerData.farmID).removeOnlinePlayer(playerData));

            if (FarmingPlugin.instance.farmsCache.get(playerData.farmID).onlinePlayers.size() < 1) {
                FarmData Farm = FarmingPlugin.instance.farmsCache.get(playerData.farmID);
                FarmingPlugin.instance.farmsCache.remove(playerData.farmID);
                FarmingPlugin.instance.database.update(Farm.id, Farm.toMap(), DatabaseCollection.FARMS);
            }

            FarmingPlugin.instance.database.update(player.getUniqueId(), playerData.toMap(), DatabaseCollection.PLAYERS);
        }
    }

}
