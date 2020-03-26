package net.mistwood.FarmingPlugin.Modules.Farm;

import br.net.fabiozumbi12.RedProtect.Bukkit.API.events.*;

import net.mistwood.FarmingPlugin.API.Events.CreateFarmEvent;
import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.FarmPermissionLevel;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Database.DatabaseCollection;
import net.mistwood.FarmingPlugin.Main;

import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;
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

        // If the farm has a default name (player_x), we wan't to change it (player's Farm)
        if (Farm.Name.contains (Event.getPlayer ().getName () + "_"))
            Farm.Name = String.format ("%s's Farm", Event.getPlayer ().getName ());

        PlayerData Data = Instance.PlayersCache.Get (Event.getPlayer ().getUniqueId ());
        Data.FarmID = Farm.ID;
        Data.FarmName = Event.getRegion ().getName ();
        Data.PermissionLevel = FarmPermissionLevel.Leader;
        Instance.PlayersCache.Update (Event.getPlayer ().getUniqueId (), Data);

        Instance.Database.Insert (Farm.ToMap (), DatabaseCollection.FarmsCollection);

        Instance.FarmsCache.Add (Farm.ID, Farm);

        // Send event
        CreateFarmEvent FarmEvent = new CreateFarmEvent (Farm, Event.getPlayer ());
        Bukkit.getPluginManager ().callEvent (FarmEvent);

        Helper.SendMessage (Event.getPlayer (), String.format (Messages.FarmCreated, Farm.Name));
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
