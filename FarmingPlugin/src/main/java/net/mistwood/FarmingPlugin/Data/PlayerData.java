package net.mistwood.FarmingPlugin.Data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData implements Data {

    public Player playerInstance;
    public String name;
    public UUID farmID;
    public String farmName;
    public FarmPermissionLevel permissionLevel;

    public PlayerData(Player playerInstance, String name, UUID farmID, String farmName, FarmPermissionLevel permissionLevel) {
        this.playerInstance = playerInstance;
        this.name = name;
        this.farmID = farmID;
        this.farmName = farmName;
        this.permissionLevel = permissionLevel;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("ID", playerInstance.getUniqueId().toString());
        data.put("Name", playerInstance.getName());
        data.put("FarmID", farmID != null ? farmID.toString() : "null");
        data.put("FarmName", farmName != null ? farmName : "null");
        data.put("FarmPermission", permissionLevel != null ? permissionLevel.toString() : "null");

        return data;
    }

    public static PlayerData fromMap(Map<String, Object> data) {
        Player player = Bukkit.getPlayer(UUID.fromString(data.get("ID").toString()));

        return new PlayerData(
                player,
                player.getName(),
                !data.get("FarmID").toString().equals("null") ? UUID.fromString(data.get("FarmID").toString()) : null,
                !data.get("FarmName").toString().equals("null") ? data.get("FarmName").toString() : null,
                !data.get("FarmID").toString().equals("null") ? FarmPermissionLevel.valueOf(data.get("FarmPermission").toString()) : null
        );
    }

    public String getName() {
        return name;
    }

}
