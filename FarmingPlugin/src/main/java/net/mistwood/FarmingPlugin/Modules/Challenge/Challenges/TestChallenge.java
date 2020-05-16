package net.mistwood.FarmingPlugin.Modules.Challenge.Challenges;

import net.mistwood.FarmingPlugin.FarmingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Modules.Challenge.Challenge;

public class TestChallenge extends Challenge {

    private static final String NAME = "TestChallenge";
    private static final String DESCRIPTION = "A test challenge";
    private static final int ID = 3402449;
    private static final int XP_REWARD = 100;

    public TestChallenge() {
        super(NAME, DESCRIPTION, ID, XP_REWARD);
    }

    @Override
    public void perform(Event event, FarmData farm) {
        // This check here is optional
        if (event instanceof PlayerAdvancementDoneEvent) {
            PlayerAdvancementDoneEvent e = (PlayerAdvancementDoneEvent) event;

            Advancement advancement = e.getAdvancement();
            Bukkit.getLogger().info(advancement.toString());

            farm.completedChallenges.add(ID);
            FarmingPlugin.instance.farmsCache.update(farm.id, farm);
        }
    }

}
