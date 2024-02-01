package me.munchii.igloolib.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public record InventoryClickEventContext(ClickType clickType, Player player, Inventory inventory, int slot) {
}
