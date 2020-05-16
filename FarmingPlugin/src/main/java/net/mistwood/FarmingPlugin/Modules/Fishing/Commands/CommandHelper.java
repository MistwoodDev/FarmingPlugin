package net.mistwood.FarmingPlugin.Modules.Fishing.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;

public class CommandHelper {

    public static void handleHelp(Player player, String[] args) {
    }

    public static void handleGive(Player player, String[] args) {
        if (args.length < 2 || args.length > 3) {
            Helper.sendMessage(player, Messages.FISHING_ERROR_GIVE_ARGS);
        } else {
            if (Bukkit.getPlayer(args[0]) == null) {
                Helper.sendMessage(player, String.format(Messages.ERROR_PLAYER_NOT_FOUND, args[0]));
            }

            int amount = 1;
            if (args.length == 3) amount = Integer.parseInt(args[2]);
            PlayerInventory inv = player.getInventory();
            ItemStack giveItem = null;

            switch (args[1].toUpperCase()) {
                case "FARMER_ROD": {
                    giveItem = new ItemStack(Material.FISHING_ROD, amount);
                    List<String> lore = new ArrayList<String>();
                    lore.add("Fishing rod - Farmer kit");
                    ItemMeta itm = giveItem.getItemMeta();
                    assert itm != null;
                    itm.setLore(lore);
                    giveItem.setItemMeta(itm);

                    break;
                }
                case "LORD_ROD": {
                    giveItem = new ItemStack(Material.FISHING_ROD, amount);
                    giveItem.addUnsafeEnchantment(Enchantment.LURE, 2);
                    giveItem.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
                    giveItem.addUnsafeEnchantment(Enchantment.LUCK, 2);
                    List<String> lore = new ArrayList<String>();
                    lore.add("Fishing rod - Lord kit");
                    lore.add("");
                    lore.add("LOOT: 2X MULTIPLIER");
                    lore.add("EXTRA LOOT 2");
                    ItemMeta itm = giveItem.getItemMeta();
                    assert itm != null;
                    itm.setLore(lore);
                    itm.setDisplayName(Helper.colorfy("&cLord Rod"));
                    giveItem.setItemMeta(itm);

                    break;
                }
                case "DEV_ROD": {
                    giveItem = new ItemStack(Material.FISHING_ROD, amount);
                    giveItem.addUnsafeEnchantment(Enchantment.LURE, 5);
                    giveItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                    giveItem.addUnsafeEnchantment(Enchantment.LUCK, 5);
                    List<String> lore = new ArrayList<String>();
                    lore.add("Fishing rod - Dev");
                    lore.add("");
                    lore.add("LOOT: 5X MULTIPLIER");
                    lore.add("EXTRA LOOT 5");
                    ItemMeta itm = giveItem.getItemMeta();
                    assert itm != null;
                    itm.setLore(lore);
                    itm.setDisplayName(Helper.colorfy("&cD&6e&ev &aR&bo&3d"));
                    giveItem.setItemMeta(itm);

                    break;
                }
                default: {
                    Helper.sendMessage(player, String.format(Messages.ERROR_UNKNOWN_ITEM, args[1]));
                    break;
                }
            }

            if (giveItem != null) {
                inv.addItem(giveItem);
            }
        }
    }

}
