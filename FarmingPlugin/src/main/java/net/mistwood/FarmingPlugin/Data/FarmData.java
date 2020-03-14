package net.mistwood.FarmingPlugin.Data;

import br.net.fabiozumbi12.RedProtect.Bukkit.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FarmData
{

    public UUID ID;
    public String Name;
    public UUID Owner;
    public Region RegionInstance;
    public List<UUID> Players;
    public List<PlayerData> OnlinePlayers;

    public FarmData (UUID ID, String Name, UUID Owner, Region RegionInstance)
    {
        this.ID = ID;
        this.Name = Name;
        this.Owner = Owner;
        this.RegionInstance = RegionInstance;
        this.Players = new ArrayList<UUID> ();
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

    public void AddOnlinePlayer (PlayerData Player)
    {
        if (!OnlinePlayers.contains (Player))
            OnlinePlayers.add (Player);
    }

    public void RemoveOnlinePlayer (PlayerData Player)
    {
        if (OnlinePlayers.contains (Player))
            OnlinePlayers.remove (Player);
    }

}
