package net.mistwood.FarmingPlugin.Modules.Challenge;

import org.bukkit.event.Event;

import net.mistwood.FarmingPlugin.Data.FarmData;

public abstract class Challenge {

    public String name;
    public String description;
    public int id;
    public int xpReward;

    public Challenge(String name, String description, int id, int xpReward) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.xpReward = xpReward;
    }

    public abstract void perform(Event event, FarmData farm);

}
