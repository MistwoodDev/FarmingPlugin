  package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class FishingEvents implements Listener
{

    private static Main Instance;

    public FishingEvents(Main Instance)
    {
        this.Instance = Instance;

        Bukkit.getServer().getPluginManager().registerEvents(this, Instance);
    }
    
    @EventHandler
    public void onFish(PlayerFishEvent Event) {
    	//TODO: Handle event
    	Player p = Event.getPlayer();
    	Material item = p.getItemInHand();
    	if (item.getType() == Material.FISHING_ROD) {
    		switch(item.getItemMeta().getDisplayName()) {
    		case "Fishing Rod":
    			Event.getHook().setBiteChance(0.08);
    		break;
    		case "God Rod":
    			Event.getHook().setBiteChance(1);
    		break;
    		default:
    		break;
    		}
    	}
    }

}
