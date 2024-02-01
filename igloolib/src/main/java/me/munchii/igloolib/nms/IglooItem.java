package me.munchii.igloolib.nms;

import me.munchii.igloolib.util.NBTUtil;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class IglooItem {
    private final ItemStack nmsStack;
    private NbtCompound nbtCompound;

    public IglooItem(ItemStack stack) {
        this.nmsStack = stack;
    }

    public static IglooItem of(org.bukkit.inventory.ItemStack stack) {
        return new IglooItem(NBTUtil.getNMSStack(stack));
    }

    public NbtCompound getOrCreateNbt() {
        if (nbtCompound == null) {
            nbtCompound = new NbtCompound(NBTUtil.getItemNBT(nmsStack));
        }

        return nbtCompound;
    }

    public void setNbt(@Nullable NbtCompound compound) {
        nbtCompound = compound;
    }

    private void prepare() {
        NBTUtil.putItemNBT(nmsStack, nbtCompound.getCompound());
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
