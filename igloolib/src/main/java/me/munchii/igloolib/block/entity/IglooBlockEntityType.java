package me.munchii.igloolib.block.entity;

import com.google.common.collect.ImmutableSet;
import me.munchii.igloolib.block.IglooBlock;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class IglooBlockEntityType<T extends IglooBlockEntity> {
    private final BlockEntityFactory<? extends T> factory;
    private final Set<IglooBlock> blocks;

    @Nullable
    public static NamespacedKey getId(IglooBlockEntityType<?> type) {
        return IglooRegistry.BLOCK_ENTITY_TYPE.getId(type);
    }

    private static <T extends IglooBlockEntity> IglooBlockEntityType<T> create(NamespacedKey key, Builder<T> builder) {
        if (builder.blocks.isEmpty()) {
            Logger.warning("IglooEntityType: Block entity type " + key.getKey() + " requires at least one valid block to be defined!");
        }

        return (IglooBlockEntityType<T>) IglooRegistry.BLOCK_ENTITY_TYPE.register(key, builder.build());
    }

    public IglooBlockEntityType(BlockEntityFactory<? extends T> factory, Set<IglooBlock> blocks) {
        this.factory = factory;
        this.blocks = blocks;
    }

    @Nullable
    public T instantiate(Location pos) {
        return this.factory.create(pos);
    }

    public boolean supports(IglooBlock block) {
        return blocks.contains(block);
    }

    public boolean supports(Material material) {
        return blocks.stream().anyMatch(block -> block.getType() == material);
    }

    public static final class Builder<T extends IglooBlockEntity> {
        private final BlockEntityFactory<? extends T> factory;
        final Set<IglooBlock> blocks;

        private Builder(BlockEntityFactory<? extends T> factory, Set<IglooBlock> blocks) {
            this.factory = factory;
            this.blocks = blocks;
        }

        public static <T extends IglooBlockEntity> Builder<T> create(BlockEntityFactory<? extends T> factory, IglooBlock... blocks) {
            return new Builder<>(factory, ImmutableSet.copyOf(blocks));
        }

        public IglooBlockEntityType<T> build() {
            return new IglooBlockEntityType<>(factory, blocks);
        }
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends IglooBlockEntity> {
        T create(Location pos);
    }
}
