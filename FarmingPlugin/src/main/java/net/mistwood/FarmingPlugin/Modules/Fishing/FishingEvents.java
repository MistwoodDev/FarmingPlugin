  package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import java.lang.Object
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerFishEvent

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
    	if (Event.State == "FISHING") {
	    	if (item.getType() == Material.FISHING_ROD) {
	    		switch(item.getItemMeta().getDisplayName()) {
	    		case "Fishing Rod":
	    			Event.getHook().setBiteChance(0.08);
	    		break;
	    		case "§bGod Rod":
	    			Event.getHook().setBiteChance(1);
	    		break;
	    		default:
	    		break;
	    		}
	    	}
    	}else if (Event.State == "BITE") {
    		//TODO: Add event when fish is on hook
    	}else if (Event.State == "CAUGHT_FISH") {
    		if(Event.getCaught() instanceof Item){
    			Item caught = (Item) Event.getCaught();
        		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_TOUCH, 1f, 1f);
	    		p.sendMessage("You caught §6%s§r with your %s!", caught.getItemMeta().getDisplayName(), item.getItemMeta().getDisplayName());
	        }
	    }else if (Event.State == "FAILED_ATTEMPT") {
	    	p.playSound(p.getLocation(), Sound.NOTE_BASS, 1f, 1f);
        }
    }

}
