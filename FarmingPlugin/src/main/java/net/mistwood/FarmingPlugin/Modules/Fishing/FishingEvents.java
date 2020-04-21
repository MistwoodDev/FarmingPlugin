  package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Main;

import java.lang.Object;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.Sound;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.Material;
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
    
    //@EventHandler
    public void onFish(PlayerFishEvent Event) {
        //TODO: Handle event
        Player p = Event.getPlayer();
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
        p.sendMessage("Fish rod casted");
        int xpToDrop = 0;
        ItemStack item = p.getInventory().getItemInMainHand();
        Event.setExpToDrop(xpToDrop);
        if (Event.getState().toString() == "FISHING") {
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
        }else if (Event.getState().toString() == "BITE") {
            //TODO: Add event when fish is on hook
        }else if (Event.getState().toString() == "CAUGHT_FISH") {
            if(Event.getCaught() instanceof ItemStack){
                ItemStack caught = (ItemStack) Event.getCaught();
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                p.sendMessage("You caught §6" + caught.getItemMeta().getDisplayName() + "§r with your " + item.getItemMeta().getDisplayName() + "!");
            }
        }else if (Event.getState().toString() == "FAILED_ATTEMPT") {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
        }
    }

}
