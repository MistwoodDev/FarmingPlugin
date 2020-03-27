package net.mistwood.FarmingPlugin.Modules.Challenge;

import net.mistwood.FarmingPlugin.Data.FarmData;
import org.bukkit.event.Event;

public abstract class Challenge
{

    public String Name;
    public String Description;
    public int ID;
    public int XPReward;

    public Challenge (String Name, String Description, int ID, int XPReward)
    {
        this.Name = Name;
        this.Description = Description;
        this.ID = ID;
        this.XPReward = XPReward;
    }

    public abstract void Perform (Event Event, FarmData Farm);

}
