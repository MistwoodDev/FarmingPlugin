package me.munchii.mistwoodfarming.modules.shop.gui;

import me.munchii.igloolib.gui.IInventoryGUI;
import me.munchii.igloolib.gui.InventoryActionResult;
import me.munchii.igloolib.gui.InventoryPage;
import me.munchii.igloolib.gui.slot.ButtonSlot;
import me.munchii.igloolib.gui.slot.StaticSlot;
import me.munchii.igloolib.util.Chat;
import me.munchii.igloolib.util.Logger;
import me.munchii.igloolib.util.StringUtil;
import me.munchii.mistwoodfarming.model.ShopItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ShopPage implements InventoryPage {
    private final List<ShopItem> items;
    private final int page;

    public ShopPage(List<ShopItem> items, int page) {
        this.items = items;
        this.page = page;
    }

    @Override
    public void draw(IInventoryGUI gui) {
        final int pageSize = gui.size() - 9;
        final int numItems = items.size();
        final int start = page * pageSize - pageSize;
        final int max = start + pageSize >= numItems ? numItems : pageSize;

        for (int i = 0; i < max; i++) {
            int idx = start + i;
            if (idx >= items.size()) {
                Logger.severe("how do i fix this man");
                break;
            }

            ShopItem item = items.get(idx);
            ItemStack shopStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(item.itemName().toUpperCase(Locale.ROOT))));
            ItemMeta shopMeta = shopStack.getItemMeta();
            if (shopMeta != null) {
                shopMeta.setDisplayName(Chat.color("&l" + StringUtil.toTitleCase(shopStack.getType().name().toLowerCase(Locale.ROOT).replace("_", " "))));
                shopMeta.setLore(List.of(
                        Chat.color("&aBuy: $" + item.buyPrice()),
                        Chat.color("&cSell: $" + item.sellPrice())));
                shopStack.setItemMeta(shopMeta);
            }

            gui.setSlot(i, new ButtonSlot(shopStack, ctx -> {
                gui.drawPage(new ItemPage(item));
                return InventoryActionResult.PASS;
            }));
        }

        if (page > 1) {
            ItemStack prevStack = new ItemStack(Material.RED_TERRACOTTA, 1);
            ItemMeta prevMeta = prevStack.getItemMeta();
            if (prevMeta != null) {
                prevMeta.setDisplayName(Chat.color("&c&l<< Prev"));
                prevStack.setItemMeta(prevMeta);
            }
            gui.setSlot(pageSize, new ButtonSlot(prevStack, ctx -> {
                gui.drawPage(new ShopPage(items, page - 1));
                return InventoryActionResult.PASS;
            }));
        } else {
            ItemStack prevStack = new ItemStack(Material.CYAN_TERRACOTTA, 1);
            ItemMeta prevMeta = prevStack.getItemMeta();
            if (prevMeta != null) {
                prevMeta.setDisplayName(Chat.color("&7&l<< Prev"));
                prevStack.setItemMeta(prevMeta);
            }
            gui.setSlot(pageSize, new StaticSlot(prevStack));
        }

        if (start + pageSize > items.size()) {
            ItemStack nextStack = new ItemStack(Material.CYAN_TERRACOTTA, 1);
            ItemMeta nextMeta = nextStack.getItemMeta();
            if (nextMeta != null) {
                nextMeta.setDisplayName(Chat.color("&7&lNext >>"));
                nextStack.setItemMeta(nextMeta);
            }
            gui.setSlot(pageSize + 1, new StaticSlot(nextStack));
        } else {
            ItemStack nextStack = new ItemStack(Material.GREEN_TERRACOTTA, 1);
            ItemMeta nextMeta = nextStack.getItemMeta();
            if (nextMeta != null) {
                nextMeta.setDisplayName(Chat.color("&a&lNext >>"));
                nextStack.setItemMeta(nextMeta);
            }
            gui.setSlot(pageSize + 1, new ButtonSlot(nextStack, ctx -> {
                gui.drawPage(new ShopPage(items, page + 1));
                return InventoryActionResult.PASS;
            }));
        }
    }
}
