package me.munchii.igloolib.gui.inventory.slot;

import me.munchii.igloolib.gui.inventory.InventoryActionResult;
import me.munchii.igloolib.gui.inventory.InventoryClickEventContext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class StaticSlot extends Slot {
    public StaticSlot(Material material) {
        super(material);
    }

    public StaticSlot(ItemStack stack) {
        super(stack);
    }

    @Override
    public InventoryActionResult onClick(InventoryClickEventContext context) {
        return InventoryActionResult.PASS;
    }
}
