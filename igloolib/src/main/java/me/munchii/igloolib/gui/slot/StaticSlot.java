package me.munchii.igloolib.gui.slot;

import me.munchii.igloolib.gui.InventoryActionResult;
import me.munchii.igloolib.gui.InventoryClickEventContext;
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
