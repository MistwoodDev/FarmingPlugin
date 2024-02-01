package me.munchii.igloolib.gui;

public enum InventoryActionResult {
    /**
     * inventory should do nothing
     */
    PASS,
    /**
     * inventory should refresh display
     */
    REFRESH,
    /**
     * inventory should close
     */
    CLOSE;
}
