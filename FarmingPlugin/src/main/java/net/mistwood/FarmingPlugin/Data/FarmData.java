package net.mistwood.FarmingPlugin.Data;

import java.util.List;
import java.util.UUID;

public class FarmData
{

    public UUID ID;
    public String Name;
    public UUID Owner;
    public List<UUID> Players;
    public List<PlayerData> OnlinePlayers;

}
