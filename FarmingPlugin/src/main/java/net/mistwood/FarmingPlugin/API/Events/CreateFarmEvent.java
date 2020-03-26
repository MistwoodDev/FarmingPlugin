package net.mistwood.FarmingPlugin.API.Events;

import net.mistwood.FarmingPlugin.Data.FarmData;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreateFarmEvent extends Event implements Cancellable
{

    private static final HandlerList Handlers = new HandlerList ();
    private final FarmData Farm;
    private final Player Player;
    private boolean IsCancelled = false;

    public CreateFarmEvent (FarmData Farm, Player Player)
    {
        this.Farm = Farm;
        this.Player = Player;
    }

    public static HandlerList GetHandlerList ()
    {
        return Handlers;
    }

    public FarmData GetFarm ()
    {
        return Farm;
    }

    public Player GetPlayer ()
    {
        return Player;
    }

    @Override
    public boolean isCancelled ()
    {
        return IsCancelled;
    }

    @Override
    public void setCancelled (boolean Cancelled)
    {
        this.IsCancelled = Cancelled;
    }

    @Override
    public HandlerList getHandlers ()
    {
        return Handlers;
    }

}
