package net.mistwood.FarmingPlugin.Modules.Challenge;

import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Challenge.Challenges.TestChallenge;

public class ChallengeModule extends Module {

    // ID Generator: https://www.random.org/integers/?num=1&min=1000000&max=9999999&col=1&base=10&format=html&rnd=new

    public ChallengeModule() {
        super("ChallengeModule", "1.0");
    }

    @Override
    public void onEnable() {
        ChallengeManager Manager = new ChallengeManager();
        Manager.addChallenge(PlayerAdvancementDoneEvent.class, new TestChallenge());

        new ChallengeEvents(Manager);
    }

    @Override
    public void onDisable() { }

}
