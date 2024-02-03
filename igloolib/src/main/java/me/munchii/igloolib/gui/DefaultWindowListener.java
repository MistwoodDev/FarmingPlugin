package me.munchii.igloolib.gui;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.gui.slot.InputSlot;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class DefaultWindowListener implements Listener {
    protected static DefaultWindowListener INSTANCE;

    public static boolean isRegistered() {
        return INSTANCE != null;
    }

    public static boolean register() {
        if (isRegistered()) {
            return false;
        }

        INSTANCE = new DefaultWindowListener();
        Bukkit.getPluginManager().registerEvents(INSTANCE, Igloolib.INSTANCE);
        return true;
    }

    public static boolean deregister() {
        if (!isRegistered()) {
            return false;
        }

        HandlerList.unregisterAll(INSTANCE);
        INSTANCE = null;
        return true;
    }

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
        // TODO: sometimes this bugs a little bit and doesnt let the player place down is own block in his own inventory for a few clicks
        if (event.getRawSlot() != event.getSlot()) {
            event.setCancelled(false);
            return;
        }

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

                                // TODO: find a way to exit the event here so we dont have to make that mess below
                                // TODO: also test if this even works
                                event.setCancelled(false);
                                gui.onClick(context);
                            } else {
                                event.setCancelled(true);
                                gui.onClick(context);
                            }
                        } else {
                            event.setCancelled(true);
                            gui.onClick(context);
                        }
                    } else {
                        event.setCancelled(true);
                        gui.onClick(context);
                    }
                } else {
                    event.setCancelled(true);
                    gui.onClick(context);
                }
            });
        }
    }
}
