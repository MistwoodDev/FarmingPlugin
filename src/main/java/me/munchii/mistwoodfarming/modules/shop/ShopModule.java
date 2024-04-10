package me.munchii.mistwoodfarming.modules.shop;

import me.munchii.igloolib.command.IglooCommand;
import me.munchii.igloolib.module.PluginModule;
import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.nms.PdcCompound;
import me.munchii.mistwoodfarming.RegistryManager;
import me.munchii.mistwoodfarming.config.MistwoodFarmingConfig;
import me.munchii.mistwoodfarming.model.ShopItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                        .withAction(ctx -> {
                            ctx.getPlayer().getInventory().addItem(RegistryManager.Blocks.SHOP_BLOCK.getDefaultStack(ctx.getPlayer().getPlayer()));
                            ctx.getPlayer().getInventory().addItem(RegistryManager.Blocks.PEDESTAL.getDefaultStack(ctx.getPlayer().getPlayer()));
                            // this here is still not working correctly
                            ctx.getPlayer().getInventory().addItem(RegistryManager.Blocks.FARMERS_CRAFTING_TABLE.getDefaultStack(ctx.getPlayer().getPlayer()));
                            ctx.getPlayer().updateInventory();

                            //SkullBlock skullBlock = SkullBlock.of("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU5ZTBmN2ZlZTRlMmNmOTRmZjM3NmYyOTlmZTg3YTcyYTE3MjM4N2VlNWJiZWM5Yzk0YjgyMWU3YmM3MDQ5NyJ9fX0=");
                            //ctx.getPlayer().getInventory().addItem(skullBlock.asItem().getItem());
                            //IglooItemStack stack = IglooItemStack.of(RegistryManager.Blocks.FARMERS_CRAFTING_TABLE);
                            //ItemStack bukkitStack = stack.asBukkitStack();
                            //SkullBlock.SkullBlockItem.getGameProfile(bukkitStack);
                            //ctx.getPlayer().giveItem(IglooItemStack.of(RegistryManager.Blocks.FARMERS_CRAFTING_TABLE.asItem().getItem()));

                            /*ItemStack stack = RegistryManager.Blocks.FARMERS_CRAFTING_TABLE.asItem().getItem();
                            ctx.getPlayer().getInventory().addItem(stack);
                            ctx.getPlayer().updateInventory();

                            IglooItemStack iStack = IglooItemStack.of(stack);
                            ctx.getPlayer().getInventory().addItem(iStack.asBukkitStack());
                            ctx.getPlayer().updateInventory();

                            ItemStack newStack = iStack.asBukkitStack();
                            ItemMeta meta = newStack.getItemMeta();
                            meta.setDisplayName("yeet");
                            newStack.setItemMeta(meta);
                            ctx.getPlayer().getInventory().addItem(newStack);
                            ctx.getPlayer().updateInventory();

                            NbtCompound nbtCompound = new NbtCompound();
                            nbtCompound.putCompound("yeet", new NbtCompound());

                            PdcCompound pdcCompound = new PdcCompound();
                            pdcCompound.putCompound("yeet", new PdcCompound());*/

                            ctx.getPlayer().giveItem(IglooItemStack.of(RegistryManager.Blocks.FARMERS_CRAFTING_TABLE));
                            ctx.getPlayer().updateInventory();


                            //ctx.getPlayer().getInventory().addItem(RegistryManager.Blocks.FARMERS_CRAFTING_TABLE.getDefaultStack(ctx.getPlayer().getPlayer()));
                            //ctx.getPlayer().updateInventory();
                            /*
                            Text.translatableColor(ctx.getPlayer().getPlayer(), "message.mistwoodfarming.open_shop").send(ctx.getPlayer().getPlayer());

                            InventoryWindow shopWindow = new InventoryWindow("Shop", 6, 9);
                            Logger.severe(items.get(0).itemName());
                            shopWindow.drawPage(new ShopPage(items, 1));
                            shopWindow.registerDefaultListener();
                            shopWindow.open(ctx.getPlayer().getPlayer());
                            */
                            return true;
                        })
                        .build());
    }

    @Override
    public void onDisable() {

    }
}
