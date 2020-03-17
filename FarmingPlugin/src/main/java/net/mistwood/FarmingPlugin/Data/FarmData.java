package net.mistwood.FarmingPlugin.Data;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import net.mistwood.FarmingPlugin.Utils.Helper;
import org.bukkit.Bukkit;

import java.util.*;

public class FarmData
{

    public UUID ID;
    public String Name;
    public String RegionName;
    public UUID Owner;
    public Region RegionInstance;
    public List<UUID> Players;
    public List<PlayerData> OnlinePlayers;

    public FarmData (UUID ID, String Name, UUID Owner, Region RegionInstance)
    {
        this (ID, Name, Owner, RegionInstance, new ArrayList<UUID> ());
    }

    public FarmData (UUID ID, String Name, UUID Owner, Region RegionInstance, List<UUID> Players)
    {
        this.ID = ID;
        this.Name = Name;
        this.RegionName = RegionInstance.getName ();
        this.Owner = Owner;
        this.RegionInstance = RegionInstance;
        this.Players = Players;
        this.OnlinePlayers = new ArrayList<PlayerData> ();

        // Add the owner to the players list
        this.Players.add (Owner);
    }

    public void AddPlayer (UUID ID)
    {
        if (!Players.contains (ID))
            Players.add (ID);
    }

    public void RemovePlayer (UUID ID)
    {
        if (Players.contains (ID))
            Players.remove (ID);
    }

    public FarmData AddOnlinePlayer (PlayerData Player)
    {
        if (!OnlinePlayers.contains (Player))
            OnlinePlayers.add (Player);

        return this;
    }

    public FarmData RemoveOnlinePlayer (PlayerData Player)
    {
        if (OnlinePlayers.contains (Player))
            OnlinePlayers.remove (Player);

        return this;
    }

    public Map<String, Object> ToMap ()
    {
        Map<String, Object> Data = new HashMap<String, Object> ();
        Data.put ("ID", ID.toString ());
        Data.put ("Name", Name);
        Data.put ("Owner", Owner.toString ());
        Data.put ("Players", Helper.UUIDListToString (Players)); // We may need to make this here into an ArrayList<Document> or something instead

        return Data;
    }

    public static FarmData FromMap (Map<String, Object> Data)
    {
        return new FarmData (
          UUID.fromString (Data.get ("ID").toString ()),
          Data.get ("Name").toString (),
          UUID.fromString (Data.get ("Owner").toString ()),
          RedProtect.get ().getAPI ().getRegion ("", Bukkit.getWorld ("world")),
          Helper.StringUUIDToUUIDList ((List<String>) Data.get ("Players"))
        );
    }

}
