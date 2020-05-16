package net.mistwood.FarmingPlugin.Modules.Challenge;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.mistwood.FarmingPlugin.FarmingPlugin;

public class ChallengeEvents implements Listener
{

    private final ChallengeManager manager;

    public ChallengeEvents (ChallengeManager manager)
    {
        this.manager = manager;

        for (Class event : manager.getEvents())
        {
            Bukkit.getPluginManager ().registerEvent (event, new Listener () {}, EventPriority.NORMAL, (listener, e) -> {
                manager.emit(e.getClass (), e, null); // TODO: Somehow find farm data here
            }, FarmingPlugin.instance);
        }
    }

    @EventHandler
    public void onPlayerAdvancementDone (PlayerAdvancementDoneEvent event)
    {
        // assuming the target player is in a farm, which we would normally check for
        manager.emit(event.getClass (), event, FarmingPlugin.instance.farmsCache.get(FarmingPlugin.instance.playersCache.get(event.getPlayer ().getUniqueId ()).farmID));
    }

}
