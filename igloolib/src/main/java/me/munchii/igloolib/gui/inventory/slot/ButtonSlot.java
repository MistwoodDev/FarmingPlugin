package me.munchii.igloolib.gui.inventory.slot;

import me.munchii.igloolib.gui.inventory.InventoryActionResult;
import me.munchii.igloolib.gui.inventory.InventoryClickEventContext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public final class ButtonSlot extends Slot {
    private final Function<InventoryClickEventContext, InventoryActionResult> onClickCallback;

    public ButtonSlot(Material material, Function<InventoryClickEventContext, InventoryActionResult> onClickCallback) {
        super(material);

        this.onClickCallback = onClickCallback;
    }

    public ButtonSlot(ItemStack stack, Function<InventoryClickEventContext, InventoryActionResult> onClickCallback) {
        super(stack);

        this.onClickCallback = onClickCallback;
    }

    @Override
    public InventoryActionResult onClick(InventoryClickEventContext context) {
        return onClickCallback.apply(context);
    }
}
