package me.munchii.igloolib.gui.inventory;

import me.munchii.igloolib.gui.inventory.slot.InputSlot;
import me.munchii.igloolib.gui.inventory.slot.Slot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class DefaultWindowListener implements Listener {
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // TODO: without this block its possible to spam click with the wrong item on an input slot, and it can still get accepted (without anything happening)
        // TODO: but it also introduces some bugs. for example sometimes it disallows moving around items in the inventory
        // TODO: but also disallows clicking with an item on a input slot. how would i go around this?
        // Maybe? https://www.spigotmc.org/threads/inventorydragevent-detect-which-inventory.489208/
        if (event.getInventory().getHolder() instanceof IInventoryGUI gui) {
            if (!gui.isDraggable()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getRawSlot() != event.getSlot()) {
            event.setCancelled(false);
            return;
        }

        if (event.getInventory().getHolder() instanceof IInventoryGUI gui) {
            InventoryClickEventContext context = new InventoryClickEventContext(event.getClick(), (Player) event.getWhoClicked(), event.getInventory(), event.getRawSlot());
            Optional<Slot> optionalSlot = gui.getSlot(event.getRawSlot());
            if (optionalSlot.isPresent()) {
                Slot slot = optionalSlot.get();
                if (slot instanceof InputSlot inputSlot) {
                    if (event.getAction() == InventoryAction.PLACE_SOME
                            || event.getAction() == InventoryAction.PLACE_ONE
                            || event.getAction() == InventoryAction.PLACE_ALL) {
                        ItemStack stack = event.getCursor();
                        if (stack != null) {
                            if (inputSlot.filter(stack)) {
                                InventoryActionResult actionResult = inputSlot.onInput(context, stack);

                                event.getWhoClicked().setItemOnCursor(null);

                                gui.handleActionResult(actionResult, context.player());
                            }
                        }
                    }
                }
            }

            event.setCancelled(true);
            gui.onClick(context);
        }
    }
}
