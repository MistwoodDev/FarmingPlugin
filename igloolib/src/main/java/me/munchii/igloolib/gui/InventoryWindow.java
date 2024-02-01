package me.munchii.igloolib.gui;

import me.munchii.igloolib.gui.slot.Slot;
import me.munchii.igloolib.gui.slot.StaticSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class InventoryWindow implements IInventoryGUI {
    private final Inventory inventory;

    private final Map<Integer, Slot> slots;
    private final String title;
    private final int rows;
    private final int columns;

    public InventoryWindow(String title, int rows, int columns) {
        this.inventory = Bukkit.createInventory(null, rows * columns, title);
        this.slots = new HashMap<>();

        this.title = title;
        this.rows = rows;
        this.columns = columns;
    }

    public void display(Player player) {
        slots.forEach((slotId, slot) -> inventory.setItem(slotId, slot.getStack()));

        player.openInventory(inventory);
    }

    public void refresh() {
        slots.forEach((slotId, slot) -> inventory.setItem(slotId, slot.getStack()));
    }

    @Override
    public void onClick(InventoryClickEventContext context) {
        if (slots.containsKey(context.slot())) {
            Slot slotItem = slots.get(context.slot());
            handleActionResult(slotItem.onClick(context), context.player());
        }
    }

    @Override
    public void handleActionResult(InventoryActionResult actionResult, Player player) {
        if (actionResult == null) {
            return;
        }

        if (actionResult == InventoryActionResult.CLOSE) {
            player.closeInventory();
        } else if (actionResult == InventoryActionResult.REFRESH) {
            refresh();
        }
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    public void fillVoidSlot(Material material) {
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            stack.setItemMeta(meta);
        }
        fillVoidSlot(stack);
    }

    public void fillVoidSlot(ItemStack stack) {
        for (int i = 0; i < inventory.getSize(); i++) {
            setSlot(i, new StaticSlot(stack));
        }
    }

    public void fill(Slot slot) {
        IntStream.range(0, inventory.getSize()).forEach(i -> setSlot(i, slot));
    }

    public void clear() {
        slots.clear();
        inventory.clear();
    }

    public void setSlot(int slotId, Slot slot) {
        slots.put(slotId, slot);
    }

    public Optional<Slot> getSlot(int slotId) {
        return slots.containsKey(slotId) ? Optional.of(slots.get(slotId)) : Optional.empty();
    }

    public int slotAmount() {
        return slots.size();
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
