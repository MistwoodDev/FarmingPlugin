package net.mistwood.FarmingPlugin.Modules.Fishing.Commands;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;

public class CommandHelper {

    public static void HandleHelp(Main Instance, Player Target, String[] Args) {
    }

    public static void HandleGive(Main Instance, Player Target, String[] Args) {
    	if (Args.length < 2 || Args.length > 3) {
    		Helper.SendMessage(Target, Messages.GiveArgsError);
    	} else {
			if (Bukkit.getPlayer(Args[0]) == null) {
				Helper.SendMessage(Target, String.format(Messages.PlayerNotFound, Args[0]));
			}

			int Amt = 1;
			if (Args.length == 3) Amt = Integer.parseInt(Args[2]);
			PlayerInventory inv = Target.getInventory();
			ItemStack giveItem = null;

			switch (Args[1].toUpperCase()) {
				case "FARMER_ROD": {
					giveItem = new ItemStack(Material.FISHING_ROD, Amt);
					List<String> lore = new ArrayList<String>();
					lore.add("Fishing rod - Farmer kit");
					ItemMeta itm = giveItem.getItemMeta();
					assert itm != null;
					itm.setLore(lore);
					giveItem.setItemMeta(itm);

					break;
				}
				case "LORD_ROD": {
					giveItem = new ItemStack(Material.FISHING_ROD, Amt);
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
					itm.setDisplayName(Helper.Colorfy("&cLord Rod"));
					giveItem.setItemMeta(itm);

					break;
				}
				case "DEV_ROD": {
					giveItem = new ItemStack(Material.FISHING_ROD, Amt);
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
					itm.setDisplayName(Helper.Colorfy("&cD&6e&ev &aR&bo&3d"));
					giveItem.setItemMeta(itm);

					break;
				}
				default: {
					Helper.SendMessage(Target, String.format(Messages.UnknownItem, Args[1]));
					break;
				}
			}

			if (giveItem != null) {
				inv.addItem(giveItem);
			}
		}
    }

}
