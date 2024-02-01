package me.munchii.igloolib.gui.slot;

import me.munchii.igloolib.gui.InventoryActionResult;
import me.munchii.igloolib.gui.InventoryClickEventContext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class InputSlot extends Slot {
    private Predicate<ItemStack> filterPredicate;
    private BiFunction<InventoryClickEventContext, ItemStack, InventoryActionResult> callback;

    public InputSlot() {
        this(stack -> true, (ctx, stack) -> InventoryActionResult.PASS);
    }

    public InputSlot(Predicate<ItemStack> filter, BiFunction<InventoryClickEventContext, ItemStack, InventoryActionResult> callback) {
        // TODO: does air work here?
        super(Material.AIR);

        this.filterPredicate = filter;
        this.callback = callback;
    }

    public boolean filter(ItemStack stack) {
        return filterPredicate.test(stack);
    }

    public InputSlot setFilter(final Predicate<ItemStack> predicate) {
        this.filterPredicate = predicate;
        return this;
    }

    public InputSlot setCallback(final BiFunction<InventoryClickEventContext, ItemStack, InventoryActionResult> callback) {
        this.callback = callback;
        return this;
    }

    public InventoryActionResult onInput(InventoryClickEventContext context, ItemStack stack) {
        return callback.apply(context, stack);
    }

    @Override
    public InventoryActionResult onClick(InventoryClickEventContext context) {
        return InventoryActionResult.PASS;
    }
}
