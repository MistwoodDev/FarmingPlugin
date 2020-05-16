package net.mistwood.FarmingPlugin.Modules.Challenge;

import org.bukkit.event.Event;

import net.mistwood.FarmingPlugin.Data.FarmData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChallengeManager {

    private final Map<Class, Challenge> challenges;

    public ChallengeManager() {
        this.challenges = new HashMap<>();
    }

    public void addChallenge(Class event, Challenge challenge) {
        challenges.put(event, challenge);
    }

    public void emit(Class event, Event eventData, FarmData farm) {
        challenges.entrySet().stream().filter(challengeEntry -> challengeEntry.getKey() == event).forEach(challenge -> challenge.getValue().perform(eventData, farm));
    }

    public Set<Class> getEvents() {
        return challenges.keySet();
    }

}
