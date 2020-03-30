package net.mistwood.FarmingPlugin.Modules.Challenge;

import net.mistwood.FarmingPlugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class ChallengeEvents implements Listener
{

    private Main Instance;
    private ChallengeManager Manager;

    public ChallengeEvents (Main Instance, ChallengeManager Manager)
    {
        this.Instance = Instance;
        this.Manager = Manager;

        for (Class Event : Manager.GetEvents ())
        {
            Bukkit.getPluginManager ().registerEvent (Event, new Listener () {}, EventPriority.NORMAL, (Listener, TargetEvent) -> {
                Manager.Emit (TargetEvent.getClass (), TargetEvent, null); // TODO: Somehow find farm data here
            }, Instance);
        }
    }

    @EventHandler
    public void onPlayerAdvancementDone (PlayerAdvancementDoneEvent Event)
    {
        // assuming the target player is in a farm, which we would normally check for
        Manager.Emit (Event.getClass (), Event, Instance.FarmsCache.Get (Instance.PlayersCache.Get (Event.getPlayer ().getUniqueId ()).FarmID));
    }

}
