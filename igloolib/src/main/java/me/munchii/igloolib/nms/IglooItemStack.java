package me.munchii.igloolib.nms;

import me.munchii.igloolib.util.NBTUtil;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class IglooItemStack {
    private final ItemStack nmsStack;
    @Nullable
    private NbtCompound nbtCompound;

    public IglooItemStack(ItemStack stack) {
        this.nmsStack = stack;
    }

    public static IglooItemStack of(org.bukkit.inventory.ItemStack stack) {
        return new IglooItemStack(NBTUtil.getNMSStack(stack));
    }

    public boolean hasNbt() {
        return nbtCompound != null && !nbtCompound.isEmpty();
    }

    @Nullable
    public NbtCompound getNbt() {
        return nbtCompound;
    }

    public NbtCompound getOrCreateNbt() {
        if (nbtCompound == null) {
            nbtCompound = new NbtCompound(NBTUtil.getItemNBT(nmsStack));
        }

        return nbtCompound;
    }

    public NbtCompound getOrCreateSubNbt(String key) {
        if (nbtCompound != null && nbtCompound.contains(key, 10)) {
            return nbtCompound.getCompound(key);
        } else {
            NbtCompound nbt = new NbtCompound();
            setSubNbt(key, nbt);
            return nbt;
        }
    }

    @Nullable
    public NbtCompound getSubNbt(String key) {
        return nbtCompound != null && nbtCompound.contains(key, 10) ? nbtCompound.getCompound(key) : null;
    }

    public void removeSubNbt(String key) {
        if (nbtCompound != null && nbtCompound.contains(key)) {
            nbtCompound.remove(key);
            if (nbtCompound.isEmpty()) {
                nbtCompound = null;
            }
        }
    }

    public void setNbt(@Nullable NbtCompound compound) {
        nbtCompound = compound;
    }

    public void setSubNbt(String key, NbtCompound nbtCompound) {
        // TODO: revisit; accept NBTBase so it can be set to anything?
        getOrCreateNbt().putCompound(key, nbtCompound);
    }

    private void prepare() {
        NBTUtil.putItemNBT(nmsStack, nbtCompound != null ? nbtCompound.getCompound() : null);
    }

    public ItemStack asNMSStack() {
        prepare();
        return nmsStack;
    }

    public org.bukkit.inventory.ItemStack asBukkitStack() {
        prepare();
        return NBTUtil.getBukkitStack(nmsStack);
    }
}
