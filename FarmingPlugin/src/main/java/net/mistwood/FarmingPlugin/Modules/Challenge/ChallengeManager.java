package net.mistwood.FarmingPlugin.Modules.Challenge;

import net.mistwood.FarmingPlugin.Data.FarmData;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public class ChallengeManager
{

    private Map<Class, Challenge> Challenges;

    public ChallengeManager ()
    {
        this.Challenges = new HashMap<Class, Challenge> ();
    }

    public void AddChallenge (Class Event, Challenge Challenge)
    {
        Challenges.put (Event, Challenge);
    }

    public void Emit (Class TargetEvent, Event EventData, FarmData Farm)
    {
        Challenges.entrySet ().stream ().filter (Value -> Value.getKey () == TargetEvent).forEach (Challenge -> Challenge.getValue ().Perform (EventData, Farm));
    }

}
