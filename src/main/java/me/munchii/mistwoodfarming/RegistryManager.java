package me.munchii.mistwoodfarming;

import me.munchii.igloolib.block.*;
import me.munchii.igloolib.block.custom.SkullBlock;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.mistwoodfarming.modules.farmerscrafting.blockentity.FarmersCraftingTableBlockEntity;
import me.munchii.mistwoodfarming.modules.pedestals.blockentity.PedestalBlockEntity;
import me.munchii.mistwoodfarming.modules.shop.blockentity.ShopBlockEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RegistryManager {
    public static NamespacedKey id(String id) {
        return new NamespacedKey(MistwoodFarming.INSTANCE, id);
    }

    public static void register() {
        registerBlocks();

        BlockEntities.init();
    }

    private static void registerBlocks() {
        Blocks.SHOP_BLOCK = registerBlock("shop_block", IglooBlock.of(Material.EMERALD_BLOCK));
        // TODO: fix skull blocks. when given like IglooItemStack.of(Blocks.FARMERS_CRAFTING_TABLE) it just gives a normal (steve) player head
        Blocks.FARMERS_CRAFTING_TABLE = registerSkullBlock("farmers_crafting_table", SkullBlock.of("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU5ZTBmN2ZlZTRlMmNmOTRmZjM3NmYyOTlmZTg3YTcyYTE3MjM4N2VlNWJiZWM5Yzk0YjgyMWU3YmM3MDQ5NyJ9fX0="));

        // Either:
        // eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDIxNmFmMjliMTlhNmZmNzUyNWQxODUzYjgxNDI3MGI4ZjA2ODU4MTBiMDg4NzBkY2UxNGJjOGYxZWE5NzQzZCJ9fX0=
        // or:
        // eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTlkMDEyMTc4MWE5Y2YwNmUzM2YyNTM0OGIzYjkxODQ4OGE3OTc0MDc2ZTMzZjRhMDI1YWY4MDBiZTdhYzliYSJ9fX0=
        Blocks.PEDESTAL = registerSkullBlock("pedestal", SkullBlock.of(""));
    }

    private static <B extends IglooBlock> B registerBlock(String name, B block) {
        IglooRegistry.BLOCK.register(id(name), block);
        IglooRegistry.ITEM.register(id(name), new IglooBlockItem(block));
        return block;
    }

    private static SkullBlock registerSkullBlock(String name, SkullBlock block) {
        IglooRegistry.BLOCK.register(id(name), block);
        IglooRegistry.ITEM.register(id(name), new SkullBlock.SkullBlockItem(block));
        return block;
    }

    public static final class Blocks {
        public static IglooBlock SHOP_BLOCK;
        public static IglooBlock FARMERS_CRAFTING_TABLE;
        public static IglooBlock PEDESTAL;
    }

    public static final class BlockEntities {
        private static final List<IglooBlockEntityType<?>> TYPES = new ArrayList<>();
        public static final IglooBlockEntityType<ShopBlockEntity> SHOP_BLOCK = register(ShopBlockEntity::new, "shop_block", Blocks.SHOP_BLOCK);
        public static final IglooBlockEntityType<FarmersCraftingTableBlockEntity> FARMERS_CRAFTING_TABLE = register(FarmersCraftingTableBlockEntity::new, "farmers_crafting_table", Blocks.FARMERS_CRAFTING_TABLE);
        public static final IglooBlockEntityType<PedestalBlockEntity> PEDESTAL = register(PedestalBlockEntity::new, "pedestal", Blocks.PEDESTAL);

        public static <T extends IglooBlockEntity> IglooBlockEntityType<T> register(Function<Location, T> supplier, String name, IglooBlock... blocks) {
            return register(id(name), IglooBlockEntityTypeBuilder.create(supplier::apply, blocks));
        }

        public static <T extends IglooBlockEntity> IglooBlockEntityType<T> register(NamespacedKey key, IglooBlockEntityTypeBuilder<T> builder) {
            IglooBlockEntityType<T> blockEntityType = builder.build();
            IglooRegistry.BLOCK_ENTITY_TYPE.register(key, blockEntityType);
            return blockEntityType;
        }

        public static void init() {
            TYPES.toString();
        }
    }
}
