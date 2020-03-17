package net.mistwood.FarmingPlugin.Data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData
{

    public Player PlayerInstance;
    public String Name;
    public UUID FarmID;
    public FarmPermissionLevel PermissionLevel;

    public PlayerData (Player PlayerInstance, String Name, UUID FarmID, FarmPermissionLevel PermissionLevel)
    {
        this.PlayerInstance = PlayerInstance;
        this.Name = Name;
        this.FarmID = FarmID;
        this.PermissionLevel = PermissionLevel;
    }

    public Map<String, Object> ToMap ()
    {
        Map<String, Object> Data = new HashMap<String, Object> ();
        Data.put ("ID", PlayerInstance.getUniqueId ().toString ());
        Data.put ("Name", PlayerInstance.getName ());
        Data.put ("FarmID", FarmID.toString ());
        Data.put ("FarmPermission", PermissionLevel.toString ());

        return Data;
    }

    public static PlayerData FromMap (Map<String, Object> Data)
    {
        Player Target = Bukkit.getPlayer (UUID.fromString (Data.get ("ID").toString ()));

        return new PlayerData (
                Target,
                Target.getName (),
                UUID.fromString (Data.get ("FarmID").toString ()),
                FarmPermissionLevel.valueOf (Data.get ("FarmPermission").toString ())
        );
    }

}
