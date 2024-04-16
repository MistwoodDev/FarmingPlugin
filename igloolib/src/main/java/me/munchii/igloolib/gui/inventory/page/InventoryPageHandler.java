package me.munchii.igloolib.gui.inventory.page;

import java.util.Stack;

public class InventoryPageHandler {
    private InventoryPage currentPage;
    private final Stack<InventoryPage> previousPages;

    public InventoryPageHandler() {
        this(null);
    }

    public InventoryPageHandler(InventoryPage currentPage) {
        this.currentPage = currentPage;
        this.previousPages = new Stack<>();
    }

    public void next(InventoryPage page) {
        previousPages.push(currentPage);
        currentPage = page;
    }

    public InventoryPage previous() {
        if (!previousPages.isEmpty()) {
            currentPage = previousPages.pop();
        }
        return currentPage;
    }

    public InventoryPage getCurrentPage() {
        return currentPage;
    }
}
