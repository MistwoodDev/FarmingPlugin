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
import me.munchii.mistwoodfarming.model.ShopItem;
import me.munchii.mistwoodfarming.modules.shop.gui.ShopPage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopModule extends PluginModule {
    public ShopModule() {
        super("shop", MistwoodFarmingConfig.shopModuleEnabled);
    }

    static List<ShopItem> items;

    static {
        items = List.of(
                new ShopItem("stone", 1.0d, 0.5d),
                new ShopItem("oak_planks", 2.0d, 1.0d),
                new ShopItem("spruce_planks", 2.0d, 1.0d),
                new ShopItem("birch_planks", 2.0d, 1.0d),
                new ShopItem("jungle_planks", 2.0d, 1.0d),
                new ShopItem("acacia_planks", 2.0d, 1.0d),
                new ShopItem("dark_oak_planks", 2.0d, 1.0d),
                new ShopItem("oak_sapling", 2.0d, 1.0d),
                new ShopItem("spruce_sapling", 2.0d, 1.0d),
                new ShopItem("birch_sapling", 2.0d, 1.0d),
                new ShopItem("jungle_sapling", 2.0d, 1.0d),
                new ShopItem("acacia_sapling", 2.0d, 1.0d),
                new ShopItem("dark_oak_sapling", 2.0d, 1.0d),
                new ShopItem("brown_mushroom", 2.0d, 1.0d),
                new ShopItem("red_mushroom", 2.0d, 1.0d),
                new ShopItem("torch", 2.0d, 1.0d),
                new ShopItem("clay", 2.0d, 1.0d),
                new ShopItem("coal", 2.0d, 1.0d),
                new ShopItem("charcoal", 2.0d, 1.0d),
                new ShopItem("diamond", 2.0d, 1.0d),
                new ShopItem("iron_ingot", 2.0d, 1.0d),
                new ShopItem("gold_ingot", 2.0d, 1.0d),
                new ShopItem("stick", 2.0d, 1.0d),
                new ShopItem("bowl", 2.0d, 1.0d),
                new ShopItem("string", 2.0d, 1.0d),
                new ShopItem("feather", 2.0d, 1.0d),
                new ShopItem("gunpowder", 2.0d, 1.0d),
                new ShopItem("wheat_seeds", 2.0d, 1.0d),
                new ShopItem("wheat", 2.0d, 1.0d),
                new ShopItem("flint", 2.0d, 1.0d),
                new ShopItem("bucket", 2.0d, 1.0d),
                new ShopItem("water_bucket", 2.0d, 1.0d),
                new ShopItem("lava_bucket", 2.0d, 1.0d),
                new ShopItem("redstone", 2.0d, 1.0d),
                new ShopItem("snowball", 2.0d, 1.0d),
                new ShopItem("leather", 2.0d, 1.0d),
                new ShopItem("milk_bucket", 2.0d, 1.0d),
                new ShopItem("brick", 2.0d, 1.0d),
                new ShopItem("sugar_cane", 2.0d, 1.0d),
                new ShopItem("kelp", 2.0d, 1.0d),
                new ShopItem("paper", 2.0d, 1.0d),
                new ShopItem("book", 2.0d, 1.0d),
                new ShopItem("slime_ball", 2.0d, 1.0d),
                new ShopItem("egg", 2.0d, 1.0d),
                new ShopItem("glowstone_dust", 2.0d, 1.0d),
                new ShopItem("cocoa_beans", 2.0d, 1.0d),
                new ShopItem("lapis_lazuli", 2.0d, 1.0d),
                new ShopItem("bone_meal", 2.0d, 1.0d),
                new ShopItem("bone", 2.0d, 1.0d),
                new ShopItem("sugar", 2.0d, 1.0d),
                new ShopItem("pumpkin_seeds", 2.0d, 1.0d),
                new ShopItem("melon_seeds", 2.0d, 1.0d),
                new ShopItem("rotten_flesh", 2.0d, 1.0d),
                new ShopItem("ender_pearl", 2.0d, 1.0d),
                new ShopItem("blaze_rod", 2.0d, 1.0d),
                new ShopItem("ghast_tear", 2.0d, 1.0d),
                new ShopItem("gold_nugget", 2.0d, 1.0d),
                new ShopItem("nether_wart", 2.0d, 1.0d),
                new ShopItem("potion", 2.0d, 1.0d),
                new ShopItem("splash_potion", 2.0d, 1.0d),
                new ShopItem("glass_bottle", 2.0d, 1.0d),
                new ShopItem("spider_eye", 2.0d, 1.0d),
                new ShopItem("fermented_spider_eye", 2.0d, 1.0d),
                new ShopItem("blaze_powder", 2.0d, 1.0d),
                new ShopItem("magma_cream", 2.0d, 1.0d),
                new ShopItem("brewing_stand", 2.0d, 1.0d),
                new ShopItem("cauldron", 2.0d, 1.0d),
                new ShopItem("ender_eye", 2.0d, 1.0d),
                new ShopItem("glistering_melon_slice", 2.0d, 1.0d),
                new ShopItem("experience_bottle", 2.0d, 1.0d),
                new ShopItem("fire_charge", 2.0d, 1.0d),
                new ShopItem("writable_book", 2.0d, 1.0d),
                new ShopItem("written_book", 2.0d, 1.0d),
                new ShopItem("emerald", 2.0d, 1.0d),
                new ShopItem("nether_star", 2.0d, 1.0d)
        );
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
                            Logger.severe(items.get(0).itemName());
                            shopWindow.renderPage(new ShopPage(items, 1));
                            shopWindow.registerDefaultListener();
                            shopWindow.open((Player) sender);

                            return true;
                        })
                        .build());
    }

    @Override
    public void onDisable() {

    }
}
