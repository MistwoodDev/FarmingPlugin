package net.mistwood.FarmingPlugin.Modules.Challenge;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Challenge.Challenges.TestChallenge;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class ChallengeModule extends Module
{

    // ID Generator: https://www.random.org/integers/?num=1&min=1000000&max=9999999&col=1&base=10&format=html&rnd=new

    private Main Instance;

    public ChallengeModule ()
    {
        super ("ChallengeModule", "1.0");
    }

    @Override
    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        ChallengeManager Manager = new ChallengeManager ();
        Manager.AddChallenge (PlayerAdvancementDoneEvent.class, new TestChallenge (Instance));

        new ChallengeEvents (Instance, Manager);
    }

    @Override
    public void OnDisable () { }

}
