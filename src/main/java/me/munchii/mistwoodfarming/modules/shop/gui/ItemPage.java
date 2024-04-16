package me.munchii.mistwoodfarming.modules.shop.gui;

import me.munchii.igloolib.gui.inventory.IInventoryGUI;
import me.munchii.igloolib.gui.inventory.page.InventoryPage;
import me.munchii.mistwoodfarming.model.ShopItem;

public class ItemPage implements InventoryPage {
    private final ShopItem item;

    public ItemPage(ShopItem item) {
        this.item = item;
    }

    @Override
    public void draw(IInventoryGUI gui) {

    }
}
