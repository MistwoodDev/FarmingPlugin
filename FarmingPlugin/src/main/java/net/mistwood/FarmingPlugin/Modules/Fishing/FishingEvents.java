package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Utils.Lores.LoreParser;
import net.mistwood.FarmingPlugin.Utils.Lores.LoreOption;
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

import java.util.*;

import static java.util.Arrays.asList;

public class FishingEvents implements Listener {

    private final static List<Material> replacements = asList(Material.COD, Material.SALMON, Material.TRIPWIRE_HOOK, Material.LILY_PAD, Material.NAME_TAG, Material.PUFFERFISH);

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

        Item caughtItem = (Item) event.getCaught();
        List<Item> fishDrops = new ArrayList<>();

        switch (event.getState()) {
            case FISHING: break;

            case BITE: {
                playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1f);
                Helper.sendMessage(player, Messages.FISHING_BITE);
                break;
            }

            case CAUGHT_FISH:
            case CAUGHT_ENTITY: {
                if (caughtItem == null) return;

                LoreOption<Integer> lootMultiplier = new LoreOption<>("Loot");
                LoreOption<Integer> extraLoot = new LoreOption<>("Extra Loot");
                LoreParser.parse(rod.getItemMeta(), lootMultiplier, extraLoot);

                if (lootMultiplier.notNull()) {
                    caughtItem.getItemStack().setAmount(lootMultiplier.getValue());
                }

                if (extraLoot.notNull()) {
                    for (int i = 0; i < extraLoot.getValue(); i++) {
                        int random = new Random().nextInt(replacements.size());
                        ItemStack extraItem = new ItemStack(replacements.get(random), caughtItem.getItemStack().getAmount());

                        Item droppedItem = caughtItem.getWorld().dropItem(caughtItem.getLocation(), extraItem);
                        Vector velocity = (player.getLocation().toVector().subtract(event.getHook().getLocation().toVector())).multiply(0.1);
                        droppedItem.setVelocity(velocity.setY(velocity.getY() + 0.2));
                        fishDrops.add(droppedItem);
                    }
                }

                fishDrops.add(caughtItem);
                playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 1f);
                playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 3f);
                playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 5f);

                Map<Material, Integer> loot = new HashMap<>();
                for (Item item : fishDrops) {
                    ItemStack stack = item.getItemStack();
                    int amount = stack.getAmount();
                    if (loot.containsKey(stack.getType())) {
                        amount += loot.get(stack.getType());
                    }
                    loot.put(stack.getType(), amount);
                }

                List<String> dropNames = new ArrayList<>();
                loot.forEach((item, amount) -> {
                    dropNames.add(amount + " " + Helper.capitalize(item.name().replace("_", " ")));
                });

                Helper.sendMessage(player, String.format(Messages.FISHING_CAUGHT_ENTITY, String.join(", ", dropNames).replaceFirst(",(?=[^,]+$)", " and"), rodName));
            }

            case FAILED_ATTEMPT: {
                playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                Helper.sendMessage(player, Messages.FISHING_BITE_FAIL);
                break;
            }
        }
    }

    private void playSound(Player player, Location location, Sound sound, float volume, float pitch) {
        if (FarmingPlugin.instance.config.fishingConfig.playSounds) {
            player.playSound(location, sound, volume, pitch);
        }
    }
}
