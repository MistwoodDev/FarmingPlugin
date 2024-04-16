package me.munchii.igloolib.gui.inventory;

import me.munchii.igloolib.gui.inventory.page.InventoryPage;
import me.munchii.igloolib.gui.inventory.page.InventoryPageHandler;
import me.munchii.igloolib.gui.inventory.slot.Slot;
import me.munchii.igloolib.gui.inventory.slot.StaticSlot;
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

    private final InventoryPageHandler pageHandler = new InventoryPageHandler();

    public InventoryWindow(String title, int rows, int columns) {
        this.inventory = Bukkit.createInventory(this, rows * columns, title);
        this.slots = new HashMap<>();

        this.title = title;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public void open(Player player) {
        slots.forEach((slotId, slot) -> inventory.setItem(slotId, slot.getStack()));

        player.openInventory(inventory);
    }

    @Override
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

    @Override
    public void fillVoidSlot(Material material) {
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            stack.setItemMeta(meta);
        }
        fillVoidSlot(stack);
    }

    @Override
    public void fillVoidSlot(ItemStack stack) {
        for (int i = 0; i < inventory.getSize(); i++) {
            setSlot(i, new StaticSlot(stack));
        }
    }

    @Override
    public void fill(Slot slot) {
        IntStream.range(0, inventory.getSize()).forEach(i -> setSlot(i, slot));
    }

    @Override
    public void clear() {
        slots.clear();
        inventory.clear();
    }

    @Override
    public void setSlot(int slotId, Slot slot) {
        slots.put(slotId, slot);
    }

    @Override
    public Optional<Slot> getSlot(int slotId) {
        return slots.containsKey(slotId) ? Optional.of(slots.get(slotId)) : Optional.empty();
    }

    public void drawPage(InventoryPage page) {
        pageHandler.next(page);
        clear();
        page.draw(this);
        refresh();
    }

    public void drawPreviousPage() {
        InventoryPage page = pageHandler.previous();
        clear();
        page.draw(this);
        refresh();
    }

    public InventoryPage getCurrentPage() {
        return pageHandler.getCurrentPage();
    }

    @Override
    public int size() {
        return inventory.getSize();
    }

    @Override
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
