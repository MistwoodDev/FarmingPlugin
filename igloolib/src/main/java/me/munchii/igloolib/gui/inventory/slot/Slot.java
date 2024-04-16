package me.munchii.igloolib.gui.inventory.slot;

import me.munchii.igloolib.gui.inventory.InventoryActionResult;
import me.munchii.igloolib.gui.inventory.InventoryClickEventContext;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class Slot {
    private ItemStack stack;
    private int amount;

    public Slot(Material type) {
        this(new ItemStack(type, 1));
    }

    public Slot(ItemStack stack) {
        this.stack = stack;
        this.amount = stack.getAmount();
    }

    public abstract InventoryActionResult onClick(InventoryClickEventContext context);

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        stack.setAmount(amount);
    }

    public void setTitle(String title) {
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
        stack.setItemMeta(meta);
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    public void setMaterial(Material material) {
        stack.setType(material);
    }
}
