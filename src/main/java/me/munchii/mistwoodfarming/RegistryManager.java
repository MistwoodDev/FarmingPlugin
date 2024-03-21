package me.munchii.mistwoodfarming;

import me.munchii.igloolib.block.*;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.util.Logger;
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
    }

    private static <B extends IglooBlock> B registerBlock(String name, B block) {
        IglooRegistry.BLOCK.register(id(name), block);
        IglooRegistry.ITEM.register(id(name), new IglooBlockItem(block));
        return block;
    }

    public static final class Blocks {
        public static IglooBlock SHOP_BLOCK;
    }

    public static final class BlockEntities {
        private static final List<IglooBlockEntityType<?>> TYPES = new ArrayList<>();
        public static final IglooBlockEntityType<ShopBlockEntity> SHOP_BLOCK = register(ShopBlockEntity::new, "shop_block", Blocks.SHOP_BLOCK);

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
