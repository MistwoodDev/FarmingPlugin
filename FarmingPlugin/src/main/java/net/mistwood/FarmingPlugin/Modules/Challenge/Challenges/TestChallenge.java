package net.mistwood.FarmingPlugin.Modules.Challenge.Challenges;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Modules.Challenge.Challenge;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class TestChallenge extends Challenge
{

    private Main Instance;

    private static final String Name = "TestChallenge";
    private static final String Description = "A test challenge";
    private static final int ID = 3402449;
    private static final int XPReward = 100;

    public TestChallenge (Main Instance)
    {
        super (Name, Description, ID, XPReward);

        this.Instance = Instance;
    }

    @Override
    public void Perform (Event Event, FarmData Farm)
    {
        // This check here is optional
        if (Event instanceof PlayerAdvancementDoneEvent)
        {
            PlayerAdvancementDoneEvent NewEvent = (PlayerAdvancementDoneEvent) Event;

            Advancement Adv = NewEvent.getAdvancement ();
            Bukkit.getLogger ().info (Adv.toString ());

            Farm.CompletedChallenges.add (ID);
            Instance.FarmsCache.Update (Farm.ID, Farm);
        }
    }

}
