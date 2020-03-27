package net.mistwood.FarmingPlugin.Modules.Challenge;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Challenge.Challenges.TestChallenge;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class ChallengeModule implements Module
{

    // ID Generator: https://www.random.org/integers/?num=1&min=1000000&max=9999999&col=1&base=10&format=html&rnd=new

    private Main Instance;

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

    @Override
    public String GetName ()
    {
        return "ChallengeModule";
    }

}
