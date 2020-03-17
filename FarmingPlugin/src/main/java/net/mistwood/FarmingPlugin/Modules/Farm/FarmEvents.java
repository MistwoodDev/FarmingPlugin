package net.mistwood.FarmingPlugin.Modules.Farm;

import br.net.fabiozumbi12.RedProtect.Bukkit.API.events.*;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Main;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class FarmEvents implements Listener
{

    private static Main Instance;

    public FarmEvents (Main Instance)
    {
        this.Instance = Instance;

        Bukkit.getServer ().getPluginManager ().registerEvents (this, Instance);
    }

    @EventHandler
    public void OnRegionCreated (CreateRegionEvent Event)
    {
        FarmData Farm = new FarmData (
                UUID.randomUUID (),
                Event.getRegion ().getName (),
                Event.getPlayer ().getUniqueId (),
                Event.getRegion ()
        );
        Farm.AddOnlinePlayer (Instance.PlayersCache.Get (Event.getPlayer ().getUniqueId ()));

        // TODO: Add the farm to the database

        Instance.FarmsCache.Add (Event.getPlayer ().getUniqueId (), Farm);
    }

    @EventHandler
    public void OnRegionDeleted (DeleteRegionEvent Event)
    {
        // TODO: Make this
    }

    @EventHandler
    public void OnRegionRenamed (RenameRegionEvent Event)
    {
        if (!Event.getOldName ().equals (Event.getNewName ()))
            // TODO: Make sure this actually works
            Instance.FarmsCache.Get (Instance.PlayersCache.Get (Event.getPlayer ().getUniqueId ()).FarmID).Name = Event.getNewName ();
    }

    @EventHandler
    public void OnRegionFlagChanged (ChangeRegionFlagEvent Event)
    {
        // TODO: Make this
    }

    @EventHandler
    public void OnRegionEnterExit (EnterExitRegionEvent Event)
    {
        // TODO: Make this
    }

}