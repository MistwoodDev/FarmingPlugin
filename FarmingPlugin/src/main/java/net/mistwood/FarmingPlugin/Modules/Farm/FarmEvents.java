package net.mistwood.FarmingPlugin.Modules.Farm;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.net.fabiozumbi12.RedProtect.Bukkit.API.events.*;

import net.mistwood.FarmingPlugin.API.Events.CreateFarmEvent;
import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.FarmPermissionLevel;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Database.DatabaseCollection;
import net.mistwood.FarmingPlugin.FarmingPlugin;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;

import java.util.UUID;

public class FarmEvents implements Listener {

    public FarmEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, FarmingPlugin.instance);
    }

    @EventHandler
    public void onRegionCreated(CreateRegionEvent event) {
        FarmData farmData = new FarmData(
                UUID.randomUUID(),
                event.getRegion().getName(),
                event.getPlayer().getUniqueId(),
                event.getRegion()
        );
        farmData.addOnlinePlayer(FarmingPlugin.instance.playersCache.get(event.getPlayer().getUniqueId()));

        // If the farm has a default name (player_x), we want to change it (player's Farm)
        if (farmData.name.contains(event.getPlayer().getName() + "_"))
            farmData.name = String.format("%s's Farm", event.getPlayer().getName());

        PlayerData playerData = FarmingPlugin.instance.playersCache.get(event.getPlayer().getUniqueId());
        playerData.farmID = farmData.id;
        playerData.farmName = event.getRegion().getName();
        playerData.permissionLevel = FarmPermissionLevel.LEADER;
        FarmingPlugin.instance.playersCache.update(event.getPlayer().getUniqueId(), playerData);

        FarmingPlugin.instance.database.insert(farmData.toMap(), DatabaseCollection.FARMS);

        FarmingPlugin.instance.farmsCache.add(farmData.id, farmData);

        // Send event
        CreateFarmEvent farmEvent = new CreateFarmEvent(farmData, event.getPlayer());
        Bukkit.getPluginManager().callEvent(farmEvent);

        Helper.sendMessage(event.getPlayer(), String.format(Messages.FARMING_FARM_CREATED, farmData.name));
    }

    @EventHandler
    public void onRegionDeleted(DeleteRegionEvent event) {
        PlayerData playerData = FarmingPlugin.instance.playersCache.get(event.getPlayer().getUniqueId());

        if (playerData.farmID != null) {
            FarmData farmData = FarmingPlugin.instance.farmsCache.get(playerData.farmID);

            if (farmData.owner == playerData.playerInstance.getUniqueId()) {
                FarmingPlugin.instance.farmsCache.remove(farmData.id);

                for (PlayerData player : farmData.onlinePlayers) {
                    player.farmID = null;
                    player.farmName = null;
                    player.permissionLevel = null;
                    FarmingPlugin.instance.playersCache.update(player.playerInstance.getUniqueId(), player);
                }

                FarmingPlugin.instance.database.remove(farmData.id, DatabaseCollection.FARMS);
            }
        }
    }

    @EventHandler
    public void onRegionRenamed(RenameRegionEvent event) {
        if (!event.getOldName().equals(event.getNewName())) {
            PlayerData player = FarmingPlugin.instance.playersCache.get(event.getPlayer().getUniqueId());
            if (player.farmID != null && player.permissionLevel == FarmPermissionLevel.LEADER) {
                FarmData farmData = FarmingPlugin.instance.farmsCache.get(player.farmID);
                farmData.name = FarmingPlugin.instance.filter.filter(event.getNewName()).getSafeName();
                FarmingPlugin.instance.farmsCache.update(farmData.id, farmData);
            }
        }
    }

    @EventHandler
    public void onRegionFlagChanged(ChangeRegionFlagEvent event) {
        // TODO: Make this
    }

    @EventHandler
    public void onRegionEnterExit(EnterExitRegionEvent event) {
        // TODO: Make this
    }

}
