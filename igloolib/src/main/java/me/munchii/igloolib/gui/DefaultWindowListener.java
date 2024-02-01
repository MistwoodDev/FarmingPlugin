package me.munchii.igloolib.gui;

import me.munchii.igloolib.gui.slot.InputSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class DefaultWindowListener implements Listener {
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof IInventoryGUI gui) {
            if (!gui.draggable()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof IInventoryGUI gui) {
            InventoryClickEventContext context = new InventoryClickEventContext(event.getClick(), (Player) event.getWhoClicked(), event.getInventory(), event.getRawSlot());
            gui.getSlot(event.getRawSlot()).ifPresent(slot -> {
                if (slot instanceof InputSlot inputSlot) {
                    if (event.getAction() == InventoryAction.PLACE_SOME) {
                        ItemStack stack = event.getCursor();
                        if (stack != null) {
                            if (inputSlot.filter(stack)) {
                                InventoryActionResult actionResult = inputSlot.onInput(context, stack);
                                event.getWhoClicked().getInventory().remove(stack);
                                ((Player) event.getWhoClicked()).updateInventory();

                                // TODO: is it safe to do this here?
                                gui.handleActionResult(actionResult, context.player());
                            }
                        }
                    }
                }
            });

            event.setCancelled(true);

            gui.onClick(context);
        }
    }
}
