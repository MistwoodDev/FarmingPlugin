package me.munchii.mistwoodfarming.modules.shop.gui;

import me.munchii.igloolib.gui.IInventoryGUI;
import me.munchii.igloolib.gui.InventoryPage;
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
