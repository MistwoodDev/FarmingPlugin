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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import net.mistwood.FarmingPlugin.Utils.Messages;
import java.util.Random;

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
        int xpToDrop = 50;
        ItemStack rod = p.getInventory().getItemInMainHand();
        String rodName = rod.getItemMeta().hasDisplayName() ? rod.getItemMeta().getDisplayName() : capitalize(rod.getType().name().replace("_", " ").toLowerCase());
        Event.setExpToDrop(xpToDrop);
        if (Event.getState() == PlayerFishEvent.State.FISHING) {
            if (rod.getType() == Material.FISHING_ROD) {
                if (rodName == "Fishing Rod") {
                    //TODO: stuff
                }else if (rodName == ChatColor.AQUA + "God Rod") {
                    //TODO: more stuff
                }
            }
        }else if (Event.getState() == PlayerFishEvent.State.BITE) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.Bite));
        }else if (Event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY || Event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
        	if(Event.getCaught() == null) return;
            Item caught = (Item) Event.getCaught();
            Material[] replacements = {Material.COD, Material.SALMON, Material.TRIPWIRE_HOOK, Material.LILY_PAD, Material.NAME_TAG};
            if (caught.getItemStack().getType() == Material.ENCHANTED_BOOK) {
            	int random = new Random().nextInt(replacements.length);
            	caught.getItemStack().setType(replacements[random]);
            }
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 3f);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 5f);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(Messages.CaughtEntity, caught.getName(), rodName)));
        }else if (Event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BiteFail));
        }
    }

	private String capitalize(String str) {
	    String words[] = str.split("\\s");  
	    String capitalizeWord = "";  
	    for(String w:words){  
	        String first = w.substring(0,1);  
	        String afterfirst = w.substring(1);  
	        capitalizeWord += first.toUpperCase() + afterfirst + " ";  
	    }  
	    return capitalizeWord.trim();  
	}
}