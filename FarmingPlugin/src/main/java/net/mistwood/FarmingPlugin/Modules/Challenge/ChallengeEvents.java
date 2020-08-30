package net.mistwood.FarmingPlugin.Modules.Challenge;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.mistwood.FarmingPlugin.FarmingPlugin;

public class ChallengeEvents implements Listener
{

    public ChallengeEvents (ChallengeManager manager)
    {
        for (Class event : manager.getEvents())
        {
            Bukkit.getPluginManager().registerEvent(event, new Listener () {}, EventPriority.NORMAL, (listener, e) -> {
                manager.emit(
                    e.getClass (),
                    e,
                    FarmingPlugin.instance.farmsCache.get(FarmingPlugin.instance.playersCache.get(event.getPlayer().getUniqueId()).farmID)
                ); // TODO: Test this method of getting farm data
            }, FarmingPlugin.instance);
        }
    }

}
