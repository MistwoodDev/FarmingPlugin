package me.munchii.mistwoodfarming.modules.farming;

import br.net.fabiozumbi12.RedProtect.Bukkit.API.events.*;
import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import me.munchii.igloolib.util.Chat;
import me.munchii.mistwoodfarming.MistwoodFarming;
import me.munchii.mistwoodfarming.model.FarmData;
import me.munchii.mistwoodfarming.model.FarmPermissionLevel;
import me.munchii.mistwoodfarming.model.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class FarmingListener implements Listener {
    @EventHandler
    public void onRegionCreated(CreateRegionEvent event) {
        PlayerData playerData = MistwoodFarming.INSTANCE.getPlayerCache().get(event.getPlayer().getUniqueId());
        if (playerData.farmId() != null) {
            event.getPlayer().sendMessage(Chat.color("&4You are already in a farm"));
            RedProtect.get().getRegionManager().remove(event.getRegion(), event.getRegion().getWorld());
            event.setCancelled(true);
            return;
        }

        String farmName;
        if (event.getRegion().getName().contains(event.getPlayer().getName() + "_")) {
            farmName = String.format("%s's Farm", event.getPlayer().getName());
        } else {
            farmName = event.getRegion().getName();
        }

        FarmData farmData = new FarmData(
                UUID.randomUUID(),
                farmName,
                event.getRegion().getName(),
                event.getPlayer().getUniqueId(),
                new HashSet<>()
        );
        farmData.members().add(event.getPlayer().getUniqueId());

        PlayerData newPlayerData = new PlayerData(
                playerData.id(),
                playerData.name(),
                farmData.id(),
                farmData.regionName(),
                FarmPermissionLevel.OWNER
        );
        MistwoodFarming.INSTANCE.getPlayerCache().replace(playerData.id(), newPlayerData);

        // TODO: insert farm data into db
        MistwoodFarming.INSTANCE.getFarmCache().put(farmData.id(), farmData);

        event.getPlayer().sendMessage(Chat.color("&aFarm created " + farmName));
    }

    @EventHandler
    public void onRegionDeleted(DeleteRegionEvent event) {
        PlayerData playerData = MistwoodFarming.INSTANCE.getPlayerCache().get(event.getPlayer().getUniqueId());
        if (playerData.farmId() != null) {
            FarmData farmData = MistwoodFarming.INSTANCE.getFarmCache().get(playerData.farmId());
            if (farmData.owner() == playerData.id()) {
                MistwoodFarming.INSTANCE.getFarmCache().remove(farmData.id());
                // TODO: set farmid and farmname to null for all online players
                // https://github.com/MistwoodDev/FarmingPlugin/blob/master/FarmingPlugin/src/main/java/net/mistwood/FarmingPlugin/Modules/Farm/FarmEvents.java#L67

                // TODO: remove farmdata from database
            }
        }
    }

    @EventHandler
    public void onRegionRenamed(RenameRegionEvent event) {
        if (!Objects.equals(event.getOldName(), event.getNewName())) {
            PlayerData playerData = MistwoodFarming.INSTANCE.getPlayerCache().get(event.getPlayer().getUniqueId());
            if (playerData.farmId() != null && playerData.permissionLevel() == FarmPermissionLevel.OWNER) {
                FarmData farmData = MistwoodFarming.INSTANCE.getFarmCache().get(playerData.farmId());
                FarmData newFarmData = new FarmData(
                        farmData.id(),
                        event.getNewName(),
                        farmData.regionName(), // TODO: should this also update to the new name when we cancel the event? testing needs to be done
                        farmData.owner(),
                        farmData.members()
                );
                MistwoodFarming.INSTANCE.getFarmCache().replace(farmData.id(), newFarmData);
            }
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onRegionFlagChanged(ChangeRegionFlagEvent event) {

    }

    @EventHandler
    public void onRegionEnterExit(EnterExitRegionEvent event) {

    }
}
