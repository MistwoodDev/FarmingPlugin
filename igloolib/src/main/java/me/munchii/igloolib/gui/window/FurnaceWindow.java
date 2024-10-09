package me.munchii.igloolib.gui.window;

import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.player.IglooPlayer;
import me.munchii.igloolib.util.Chat;
import me.munchii.igloolib.util.TaskManager;
import me.munchii.igloolib.util.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public final class FurnaceWindow implements IglooWindow {
    private String title;
    private final Consumer<FurnaceWindow> tickTask;
    private FurnaceInventory inventory;

    public FurnaceWindow(String title) {
        this(title, window -> {});
    }

    public FurnaceWindow(String title, Consumer<FurnaceWindow> tickTask) {
        this.title = Chat.color(title);
        this.tickTask = tickTask;
        this.inventory = createInventory();
    }

    public void setDisplayName(String title) {
        this.title = Chat.color(title);
        this.inventory = createInventory();
    }

    @Override
    public String getDisplayName() {
        return title;
    }

    @Override
    public InventoryView open(@NotNull IglooPlayer player) {
        update();

        TaskManager.registerRepeatingTask("FurnaceWindow_Tick_" + hashCode(), () -> tickTask.accept(this), 1, 1, TimeUnit.TICK);

        return player.openInventory(inventory);
    }

    @Override
    public void close(@NotNull IglooPlayer player) {
        TaskManager.removeTask("FurnaceWindow_Tick_" + hashCode());
    }

    @Override
    public void update() {

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @NotNull
    public IglooItemStack getInputSlot() {
        ItemStack stack = inventory.getSmelting();
        return stack == null ? IglooItemStack.of(Material.AIR) : IglooItemStack.of(stack);
    }

    public void setInput(@Nullable ItemStack stack) {
        inventory.setSmelting(stack);
    }

    @NotNull
    public IglooItemStack getFuelSlot() {
        ItemStack stack = inventory.getFuel();
        return stack == null ? IglooItemStack.of(Material.AIR) : IglooItemStack.of(stack);
    }

    public void setFuel(@Nullable ItemStack stack) {
        inventory.setFuel(stack);
    }

    @NotNull
    public IglooItemStack getOutputSlot() {
        ItemStack stack = inventory.getResult();
        return stack == null ? IglooItemStack.of(Material.AIR) : IglooItemStack.of(stack);
    }

    public void setOutput(@Nullable ItemStack stack) {
        inventory.setResult(stack);
    }

    @NotNull
    public List<HumanEntity> getViewers() {
        return inventory.getViewers();
    }

    private FurnaceInventory createInventory() {
        return (FurnaceInventory) Bukkit.createInventory(this, InventoryType.FURNACE, title);
    }
}
