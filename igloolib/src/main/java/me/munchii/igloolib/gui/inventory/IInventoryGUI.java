package me.munchii.igloolib.gui.inventory;

import me.munchii.igloolib.gui.inventory.page.InventoryPage;
import me.munchii.igloolib.gui.inventory.slot.Slot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface IInventoryGUI extends InventoryHolder {
    void onClick(InventoryClickEventContext context);

    void handleActionResult(InventoryActionResult actionResult, Player player);

    default boolean isDraggable() {
        return false;
    }

    void open(Player player);

    void refresh();

    void clear();

    void fillVoidSlot(Material material);

    void fillVoidSlot(ItemStack stack);

    void fill(Slot slot);

    Optional<Slot> getSlot(int slotId);

    void setSlot(int slotId, Slot slot);

    int size();

    int slotAmount();

    default void drawPage(InventoryPage page) {
        clear();
        page.draw(this);
        refresh();
    }
}
