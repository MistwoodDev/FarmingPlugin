package net.mistwood.FarmingPlugin.Data;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData
{

    public Player PlayerInstance;
    public UUID FarmID;

    public PlayerData (Player PlayerInstance, UUID FarmID)
    {
        this.PlayerInstance = PlayerInstance;
        this.FarmID = FarmID;
    }

}
