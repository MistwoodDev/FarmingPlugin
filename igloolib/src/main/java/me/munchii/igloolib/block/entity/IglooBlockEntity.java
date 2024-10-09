package me.munchii.igloolib.block.entity;

import me.munchii.igloolib.block.IglooBlockItem;
import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class IglooBlockEntity {
    private final IglooBlockEntityType<?> blockEntityType;
    protected final Location pos;

    public IglooBlockEntity(IglooBlockEntityType<?> blockEntityType, Location pos) {
        this.blockEntityType = blockEntityType;
        this.pos = pos;
    }

    public void tick(World world, Location pos, Block block) {
    }

    public void readNbt(NbtCompound nbt) {
    }

    public void writeNbt(NbtCompound nbt) {
    }

    public IglooBlockEntityType<?> getType() {
        return blockEntityType;
    }

    public Location getPos() {
        return pos;
    }

    @Nullable
    public World getWorld() {
        return pos.getWorld();
    }

    public boolean hasWorld() {
        return pos.getWorld() != null;
    }

    public final NbtCompound createNbtWithIdentifyingData() {
        NbtCompound nbtCompound = createNbt();
        writeIdentifyingData(nbtCompound);
        return nbtCompound;
    }

    public final NbtCompound createNbtWithId() {
        NbtCompound nbtCompound = createNbt();
        writeIdToNbt(nbtCompound);
        return nbtCompound;
    }

    public final NbtCompound createNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        writeNbt(nbtCompound);
        return nbtCompound;
    }

    private void writeIdToNbt(NbtCompound nbt) {
        NamespacedKey key = IglooBlockEntityType.getId(getType());
        if (key == null) {
            throw new RuntimeException(getClass() + " is missing a mapping!");
        } else {
            nbt.putString("id", key.toString());
        }
    }

    public static void writeIdToNbt(NbtCompound nbt, IglooBlockEntityType<?> type) {
        nbt.putString("id", Objects.requireNonNull(IglooBlockEntityType.getId(type)).toString());
    }

    public void setStackNbt(ItemStack stack) {
        IglooBlockItem.setBlockEntityNbt(stack, getType(), createNbt());
    }

    public void setStackNbt(IglooItemStack stack) {
        IglooBlockItem.setBlockEntityNbt(stack, getType(), createNbt());
    }

    private void writeIdentifyingData(NbtCompound nbt) {
        writeIdToNbt(nbt);
        nbt.putInt("x", pos.getBlockX());
        nbt.putInt("y", pos.getBlockY());
        nbt.putInt("z", pos.getBlockZ());
    }

    @Nullable
    public static IglooBlockEntity createFromNbt(Location pos, NbtCompound nbt) {
        String keyString = nbt.getString("id");
        NamespacedKey key = NamespacedKey.fromString(keyString);
        if (key == null) {
            Logger.severe("Block entity has invalid type: " + keyString);
            return null;
        } else {
            return IglooRegistry.BLOCK_ENTITY_TYPE.getOrEmpty(key).map(type -> {
                try {
                    return type.instantiate(pos);
                } catch (Throwable e) {
                    Logger.severe("IglooBlockEntity: Failed to create block entity '" + keyString + "'", e);
                    return null;
                }
            }).map(blockEntity -> {
                try {
                    blockEntity.readNbt(nbt);
                    return blockEntity;
                } catch (Throwable e) {
                    Logger.severe("IglooBlockEntity: Failed to load data for block entity '" + keyString + "'", e);
                    return null;
                }
            }).orElseGet(() -> {
                Logger.warning("IglooBlockEntity: Skipping IglooBlockEntity with id '" + keyString + "'");
                return null;
            });
        }
    }
}
