package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Utils.Messages;
import net.mistwood.FarmingPlugin.Utils.Helper;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

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
        int xpToDrop = 0;

        ItemStack rod = p.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD ? p.getInventory().getItemInMainHand() : p.getInventory().getItemInOffHand();
        String rodName = rod.getItemMeta().hasDisplayName() ? rod.getItemMeta().getDisplayName() : Helper.Capitalize(rod.getType().name().replace("_", " ").toLowerCase());
        Event.setExpToDrop(xpToDrop);

        if (Event.getState() == PlayerFishEvent.State.FISHING) {
            // ...
        } else if (Event.getState() == PlayerFishEvent.State.BITE) {
            PlaySound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1f);
            Helper.SendMessage(p, Messages.Bite);
        } else if (Event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY || Event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Item caught = (Item) Event.getCaught();
            List<Item> fishDrops = new ArrayList<>();
            if(caught == null) return;

            Material[] replacements = {Material.COD, Material.SALMON, Material.TRIPWIRE_HOOK, Material.LILY_PAD, Material.NAME_TAG, Material.PUFFERFISH};

            if (rod.getItemMeta().hasLore()) {
            	if (rod.getItemMeta().getLore().size() > 1) {
            	    List<String> modifiers = rod.getItemMeta().getLore().subList(2, rod.getItemMeta().getLore().size());
            	    for (int i = 0; i < modifiers.size(); i++) {
            		String[] modifiersArray = new String[modifiers.size()];
            		modifiersArray = modifiers.toArray(modifiersArray);
	            	if (modifiersArray[i].startsWith("LOOT: ")) {
			    int lootmultiplier = Integer.parseInt(modifiersArray[i].substring(6, 7));
			    caught.getItemStack().setAmount(lootmultiplier);
	            	}
	            	if (modifiersArray[i].startsWith("EXTRA LOOT ")) {
			    int extralootlvl = Integer.parseInt(modifiersArray[i].substring(11, 12));
			    for (int ind = 0; i < extralootlvl; i++) {
			        int random = new Random().nextInt(replacements.length);
			        ItemStack extraitem = new ItemStack(replacements[random]);
			        extraitem.setAmount(caught.getItemStack().getAmount());
			        Item droppeditem = caught.getWorld().dropItem(caught.getLocation(), extraitem);
			        Vector velocity = (p.getLocation().toVector().subtract(Event.getHook().getLocation().toVector())).multiply(0.1);
			        droppeditem.setVelocity(velocity.setY(velocity.getY() + 0.2));
			        fishDrops.add(droppeditem);
			    }
	                }
            	    }
                }
            }
            fishDrops.add(caught);

            PlaySound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 1f);
            PlaySound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 3f);
            PlaySound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 5f);

            List<String> finalLoot = new ArrayList<>();
            
            for (int o = 0; o < fishDrops.size(); o++) {
    		int oldAmount = fishDrops.get(o).getItemStack().getAmount();
            	for (int c = o + 1; c < fishDrops.size(); c++) {
            	    if (c != 0 && fishDrops.get(o).getItemStack().getType() == fishDrops.get(c).getItemStack().getType()) {;
            		fishDrops.get(o).getItemStack().setAmount(fishDrops.get(o).getItemStack().getAmount() + fishDrops.get(c).getItemStack().getAmount());
            		fishDrops.remove(c);
            	    }
            	}
            	String plural = "";
            	if (fishDrops.get(o).getItemStack().getAmount() != 1) {
            	    if (fishDrops.get(o).getItemStack().getType().toString().toLowerCase().endsWith("h")) {
            		plural = "es";
            	    }else plural = "s";
            	}
            	finalLoot.add(fishDrops.get(o).getItemStack().getAmount() + " " + fishDrops.get(o).getName() + plural);
    		fishDrops.get(o).getItemStack().setAmount(oldAmount);
            }
            
            Helper.SendMessage(p, String.format(Messages.CaughtEntity, String.join(", ", finalLoot).replaceFirst(",(?=[^,]+$)", " and"), rodName));
        } else if (Event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT) {
            PlaySound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            Helper.SendMessage(p, Messages.BiteFail);
        }
    }
	
    private void PlaySound (Player p, Location location, Sound sound, float volume, float pitch) {
	if (Instance.Config.PlaySounds) {
	    p.playSound(location, sound, volume, pitch);
        }
    }
}
