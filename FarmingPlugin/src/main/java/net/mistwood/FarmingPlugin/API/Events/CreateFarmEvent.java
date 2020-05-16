package net.mistwood.FarmingPlugin.API.Events;

import net.mistwood.FarmingPlugin.Data.FarmData;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreateFarmEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final FarmData farmData;
    private final Player player;
    private boolean cancelled = false;

    public CreateFarmEvent(FarmData farm, Player player) {
        this.farmData = farm;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public FarmData getFarm() {
        return farmData;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean Cancelled) {
        this.cancelled = Cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
