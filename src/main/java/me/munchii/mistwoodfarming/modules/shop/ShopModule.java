package me.munchii.mistwoodfarming.modules.shop;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.command.IglooCommand;
import me.munchii.igloolib.gui.InventoryActionResult;
import me.munchii.igloolib.gui.InventoryWindow;
import me.munchii.igloolib.gui.slot.ButtonSlot;
import me.munchii.igloolib.gui.slot.InputSlot;
import me.munchii.igloolib.module.PluginModule;
import me.munchii.igloolib.util.Logger;
import me.munchii.mistwoodfarming.MistwoodFarming;
import me.munchii.mistwoodfarming.config.MistwoodFarmingConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ShopModule extends PluginModule {
    public ShopModule() {
        super("shop", MistwoodFarmingConfig.shopModuleEnabled);
    }

    @Override
    public void onEnable() {
        getCommandManager()
                .registerCommand(IglooCommand.create("shop")
                        .withDescription("Opens the shop window")
                        .withUsage("/shop")
                        .withAlias("sh")
                        .withAction((sender, args) -> {
                            InventoryWindow shopWindow = new InventoryWindow("Shop", 6, 9);
                            shopWindow.fillVoidSlot(Material.BLACK_STAINED_GLASS_PANE);

                            shopWindow.setSlot(0, new ButtonSlot(Material.STONE, ctx -> {
                                if (ctx.clickType() == ClickType.LEFT) {
                                    //MistwoodFarming.INSTANCE.getEconomyService().withdrawPlayer(ctx.player(), 10d);
                                    ctx.player().getInventory().addItem(new ItemStack(Material.STONE, 1));
                                    ctx.player().updateInventory();
                                } else if (ctx.clickType() == ClickType.RIGHT) {
                                    //MistwoodFarming.INSTANCE.getEconomyService().depositPlayer(ctx.player(), 10d);
                                    ctx.player().getInventory().removeItem(new ItemStack(Material.STONE, 1));
                                    ctx.player().updateInventory();
                                }

                                return InventoryActionResult.PASS;
                            }));
                            shopWindow.setSlot(1, new InputSlot()
                                    .setFilter(stack -> stack.getType() == Material.GRASS_BLOCK)
                                    .setCallback((ctx, stack) -> {
                                        ctx.player().getInventory().addItem(new ItemStack(Material.DIRT, 2));
                                        ctx.player().updateInventory();
                                        return InventoryActionResult.PASS;
                                    }));

                            shopWindow.registerDefaultListener();
                            shopWindow.display((Player) sender);

                            return true;
                        })
                        .build());
    }

    @Override
    public void onDisable() {

    }
}
