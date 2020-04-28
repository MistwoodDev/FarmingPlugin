package net.mistwood.FarmingPlugin.Data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData implements Data
{

    public Player PlayerInstance;
    public String Name;
    public UUID FarmID;
    public String FarmName;
    public FarmPermissionLevel PermissionLevel;

    public PlayerData (Player PlayerInstance, String Name, UUID FarmID, String FarmName, FarmPermissionLevel PermissionLevel)
    {
        this.PlayerInstance = PlayerInstance;
        this.Name = Name;
        this.FarmID = FarmID;
        this.FarmName = FarmName;
        this.PermissionLevel = PermissionLevel;
    }

    @Override
    public Map<String, Object> ToMap ()
    {
        Map<String, Object> Data = new HashMap<String, Object> ();
        Data.put ("ID", PlayerInstance.getUniqueId ().toString ());
        Data.put ("Name", PlayerInstance.getName ());
        Data.put ("FarmID", FarmID != null ? FarmID.toString () : "null");
        Data.put ("FarmName", FarmName != null ? FarmName : "null");
        Data.put ("FarmPermission", PermissionLevel != null ? PermissionLevel.toString () : "null");

        return Data;
    }

    public static PlayerData FromMap (Map<String, Object> Data)
    {
        Player Target = Bukkit.getPlayer (UUID.fromString (Data.get ("ID").toString ()));

        return new PlayerData (
                Target,
                Target.getName (),
                !Data.get("FarmID").toString ().equals ("null") ? UUID.fromString (Data.get ("FarmID").toString ()) : null,
                !Data.get ("FarmName").toString ().equals ("null") ? Data.get ("FarmName").toString () : null,
                !Data.get("FarmID").toString ().equals ("null") ? FarmPermissionLevel.valueOf (Data.get ("FarmPermission").toString ()) : null
        );
    }

    public String GetName ()
    {
        return Name;
    }

}
