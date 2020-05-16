package net.mistwood.FarmingPlugin.Data;

import org.bukkit.Bukkit;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;

import net.mistwood.FarmingPlugin.Utils.Helper;

import java.util.*;

public class FarmData implements Data {

    public UUID id;
    public String name;
    public String regionName;
    public UUID owner;
    public Region regionInstance;
    public List<UUID> players;
    public List<PlayerData> onlinePlayers;

    public List<Integer> completedChallenges;
    public FarmStats stats;

    public FarmData(UUID id, String name, UUID owner, Region regionInstance) {
        this(id, name, owner, regionInstance, new ArrayList<>());
    }

    public FarmData(UUID id, String name, UUID owner, Region regionInstance, List<UUID> players) {
        this(id, name, owner, regionInstance, players, new ArrayList<>(), FarmStats.init());
    }

    public FarmData(UUID id, String name, UUID owner, Region regionInstance, List<UUID> players, List<Integer> completedChallenges, FarmStats stats) {
        this.id = id;
        this.name = name;
        this.regionName = regionInstance.getName();
        this.owner = owner;
        this.regionInstance = regionInstance;
        this.players = players;
        this.onlinePlayers = new ArrayList<PlayerData>();
        this.completedChallenges = completedChallenges;
        this.stats = stats;

        // Add the owner to the players list
        this.players.add(owner);
    }

    public void addPlayer(UUID id) {
        if (!players.contains(id))
            players.add(id);
    }

    public void removePlayer(UUID id) {
        // TODO: Remove from online list too?
        players.remove(id);
    }

    public FarmData addOnlinePlayer(PlayerData player) {
        if (!onlinePlayers.contains(player))
            onlinePlayers.add(player);

        return this;
    }

    public FarmData removeOnlinePlayer(PlayerData player) {
        onlinePlayers.remove(player);
        return this;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("ID", id.toString());
        data.put("Name", name);
        data.put("Owner", owner.toString());
        data.put("Players", Helper.uuidsToStrings(players));
        data.put("Info", String.valueOf(regionInstance.getArea()));
        data.put("Date", regionInstance.getDate());
        data.put("CompletedChallenges", completedChallenges);
        data.put("Stats", stats.toMap());

        return data;
    }

    public static FarmData fromMap(Map<String, Object> data) {
        FarmData Farm = new FarmData(
                UUID.fromString(data.get("ID").toString()),
                data.get("Name").toString(),
                UUID.fromString(data.get("Owner").toString()),
                RedProtect.get().getAPI().getRegion("", Objects.requireNonNull(Bukkit.getWorld("world"))),
                Helper.stringsToUUIDs((List<String>) data.get("Players"))
                // TODO: Add completed challenges list and farm stats
        );

        // Horrible way:
        Farm.completedChallenges = (List<Integer>) data.get("CompletedChallenges");
        Farm.stats = FarmStats.fromMap((Map<String, Object>) data.get("Stats"));

        return Farm;
    }

}
