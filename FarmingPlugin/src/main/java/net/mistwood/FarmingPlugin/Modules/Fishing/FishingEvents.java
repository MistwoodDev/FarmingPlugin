package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Main;

import java.lang.Object;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.Sound;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

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
        Player p = Event.getPlayer();
        //p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
        int xpToDrop = 50;
        ItemStack rod = p.getInventory().getItemInMainHand();
        String rodName = rod.getItemMeta().hasDisplayName() ? rod.getItemMeta().getDisplayName() : capitalize(rod.getType().name().replace("_", " ").toLowerCase());
        Event.setExpToDrop(xpToDrop);
        if (Event.getState() == PlayerFishEvent.State.FISHING) {
            p.sendMessage(rodName + ChatColor.RESET + " casted");
            if (rod.getType() == Material.FISHING_ROD) {
                if (rodName == "Fishing Rod") {
                    //TODO: stuff
                }else if (rodName == ChatColor.AQUA + "God Rod") {
                    //TODO: more stuff
                }
            }
        }else if (Event.getState() == PlayerFishEvent.State.BITE) {
            //TODO: Add event when fish is on hook
        }else if (Event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY || Event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
        	if(Event.getCaught() == null) return;
            Entity caught = (Entity) Event.getCaught();
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
            p.sendMessage("You caught a " + ChatColor.GOLD + caught.getName() + ChatColor.RESET + " with your " + rodName + "!");
        }else if (Event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
        }
    }

	private String capitalize(String str) {
	    String words[] = str.split("\\s");  
	    String capitalizeWord = "";  
	    for(String w:words){  
	        String first = w.substring(0,1);  
	        String afterfirst = w.substring(1);  
	        capitalizeWord += first.toUpperCase()+afterfirst+" ";  
	    }  
	    return capitalizeWord.trim();  
	}
}
