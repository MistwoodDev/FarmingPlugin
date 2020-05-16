package net.mistwood.FarmingPlugin.Modules.Fishing;

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

import net.mistwood.FarmingPlugin.FarmingPlugin;
import net.mistwood.FarmingPlugin.Utils.Messages;
import net.mistwood.FarmingPlugin.Utils.Helper;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class FishingEvents implements Listener {

    public FishingEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(this, FarmingPlugin.instance);
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        int xpToDrop = 0;

        ItemStack rod = player.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
        String rodName = rod.getItemMeta().hasDisplayName() ? rod.getItemMeta().getDisplayName() : Helper.capitalize(rod.getType().name().replace("_", " ").toLowerCase());
        event.setExpToDrop(xpToDrop);

        if (event.getState() == PlayerFishEvent.State.FISHING) {
            // ...
        } else if (event.getState() == PlayerFishEvent.State.BITE) {
            playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1f);
            Helper.sendMessage(player, Messages.FISHING_BITE);
        } else if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY || event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Item caught = (Item) event.getCaught();
            List<Item> fishDrops = new ArrayList<>();
            if (caught == null) return;

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
                        } else if (modifiersArray[i].startsWith("EXTRA LOOT ")) {
                            int extraLootLvl = Integer.parseInt(modifiersArray[i].substring(11, 12));
                            for (int ind = 0; i < extraLootLvl; i++) {
                                int random = new Random().nextInt(replacements.length);
                                ItemStack extraItem = new ItemStack(replacements[random]);
                                extraItem.setAmount(caught.getItemStack().getAmount());
                                Item droppedItem = caught.getWorld().dropItem(caught.getLocation(), extraItem);
                                Vector velocity = (player.getLocation().toVector().subtract(event.getHook().getLocation().toVector())).multiply(0.1);
                                droppedItem.setVelocity(velocity.setY(velocity.getY() + 0.2));
                                fishDrops.add(droppedItem);
                            }
                        }
                    }
                }
            }
            fishDrops.add(caught);

            playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 1f);
            playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 3f);
            playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 5f);

            List<String> finalLoot = new ArrayList<>();

            for (int o = 0; o < fishDrops.size(); o++) {
                int oldAmount = fishDrops.get(o).getItemStack().getAmount();
                for (int c = o + 1; c < fishDrops.size(); c++) {
                    if (c != 0 && fishDrops.get(o).getItemStack().getType() == fishDrops.get(c).getItemStack().getType()) {
                        fishDrops.get(o).getItemStack().setAmount(fishDrops.get(o).getItemStack().getAmount() + fishDrops.get(c).getItemStack().getAmount());
                        fishDrops.remove(c);
                    }
                }
                String plural = "";
                if (fishDrops.get(o).getItemStack().getAmount() != 1) {
                    if (fishDrops.get(o).getItemStack().getType().toString().toLowerCase().endsWith("h")) {
                        plural = "es";
                    } else plural = "s";
                }
                finalLoot.add(fishDrops.get(o).getItemStack().getAmount() + " " + fishDrops.get(o).getName() + plural);
                fishDrops.get(o).getItemStack().setAmount(oldAmount);
            }

            Helper.sendMessage(player, String.format(Messages.FISHING_CAUGHT_ENTITY, String.join(", ", finalLoot).replaceFirst(",(?=[^,]+$)", " and"), rodName));
        } else if (event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT) {
            playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
            Helper.sendMessage(player, Messages.FISHING_BITE_FAIL);
        }
    }

    private void playSound(Player player, Location location, Sound sound, float volume, float pitch) {
        if (FarmingPlugin.instance.config.fishingConfig.playSounds) {
            player.playSound(location, sound, volume, pitch);
        }
    }
}
