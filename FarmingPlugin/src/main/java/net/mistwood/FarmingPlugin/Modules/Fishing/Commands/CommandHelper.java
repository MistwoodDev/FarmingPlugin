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

import java.util.Collections;
import java.lang.Integer;
import java.util.ArrayList;

public class CommandHelper {

    public static void HandleHelp(Main Instance, Player Sender, String Command) {

    }

    public static void HandleGive(Main Instance, Player Target, String[] Args) {
    	if (Args.length < 2 || Args.length > 3) {
    		Helper.SendMessage(Target, Messages.GiveArgsError);
    	}else {
    		if (Bukkit.getPlayer(Args[0]) != null) {
		int Amt = 1;
		if (Args.length == 3) Amt = Integer.parseInt(Args[2]);
                PlayerInventory inv = Target.getInventory();
                ItemStack giveItem = null;
                
		switch(Args[1].toUpperCase()) {
			case "FARMER_ROD":
	                    giveItem = new ItemStack(Material.FISHING_ROD, Amt);
	                    ArrayList<String> lore = new ArrayList();
	                    lore.add("Fishing rod - Farmer kit");
	                    ItemMeta itm = giveItem.getItemMeta();
	                    itm.setLore(lore);
	                    giveItem.setItemMeta(itm);
			break;
			case "GOD_ROD":
	                    giveItem = new ItemStack(Material.FISHING_ROD, Amt);
	                    giveItem.addUnsafeEnchantment(Enchantment.LURE, 3);
	                    giveItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
	                    giveItem.addUnsafeEnchantment(Enchantment.LUCK, 3);
	                    lore = new ArrayList();
	                    lore.add("Fishing rod - God kit");
	                    lore.add("");
	                    lore.add("LOOT: 3X MULTIPLIER");
	                    itm = giveItem.getItemMeta();
	                    itm.setLore(lore);
	                    itm.setDisplayName("§bGod Rod");
	                    giveItem.setItemMeta(itm);
			break;
			case "DEV_ROD":
	                    giveItem = new ItemStack(Material.FISHING_ROD, Amt);
	                    giveItem = new ItemStack(Material.FISHING_ROD, Amt);
	                    giveItem.addUnsafeEnchantment(Enchantment.LURE, 5);
	                    giveItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
	                    giveItem.addUnsafeEnchantment(Enchantment.LUCK, 5);
	                    lore = new ArrayList();
	                    lore.add("Fishing rod - Dev");
	                    lore.add("");
	                    lore.add("LOOT: 5X MULTIPLIER");
	                    itm = giveItem.getItemMeta();
	                    itm.setLore(lore);
	                    itm.setDisplayName("§r§ke§r §cD§6e§ev §aR§bo§3d§r §ke§r");
	                    giveItem.setItemMeta(itm);
			break;
			default:
				Helper.SendMessage(Target, String.format(Messages.UnknownItem, Args[1]));
			break;
		}
                if (giveItem != null) {
                    inv.addItem(giveItem);
                }
    	    }else Helper.SendMessage(Target, Messages.PlayerNotFound);
    	}
    }

    public static boolean isInt(String str) { 
    	try {  
    	    Integer.parseInt(str);  
    	    return true;
    	} catch(NumberFormatException e){  
    	    return false;  
    	}  
    }
}
