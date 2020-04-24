package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Utils.Messages;
import net.mistwood.FarmingPlugin.Utils.Helper;

import java.util.Random;

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
        	if(caught == null) return;

            Material[] replacements = {Material.COD, Material.SALMON, Material.TRIPWIRE_HOOK, Material.LILY_PAD, Material.NAME_TAG, Material.SADDLE, Material.PUFFERFISH};

            if (caught.getItemStack().getType() == Material.ENCHANTED_BOOK) {
            	int random = new Random().nextInt(replacements.length);
            	caught.getItemStack().setType(replacements[random]);
            }

            if (rod.getItemMeta().hasLore()) {
            	if (rod.getItemMeta().getLore().size() > 1) {
            		if (rod.getItemMeta().getLore().get(2).startsWith("LOOT: ")) {
		            	int multiplier = Integer.parseInt(rod.getItemMeta().getLore().get(2).substring(6, 7));
		            	caught.getItemStack().setAmount(multiplier);
            		}
            	}
        	}

            PlaySound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 1f);
            PlaySound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 3f);
            PlaySound(p, p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 5f);

            Helper.SendMessage(p, String.format(Messages.CaughtEntity, caught.getItemStack().getAmount(), caught.getName(), rodName));
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
