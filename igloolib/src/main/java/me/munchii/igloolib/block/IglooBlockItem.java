package me.munchii.igloolib.block;

import me.munchii.igloolib.item.IglooItem;
import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.nms.NbtCompound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class IglooBlockItem extends IglooItem {
    private final IglooBlock block;

    public IglooBlockItem(IglooBlock block) {
        this.block = block;
    }

    public IglooBlock getBlock() {
        return block;
    }

    public ItemStack getItem() {
        // TODO: apply nbt
        return new ItemStack(block.getType(), 1);
    }

    @Nullable
    public static NbtCompound getBlockEntityNbt(ItemStack stack) {
        return getBlockEntityNbt(IglooItemStack.of(stack));
    }

    @Nullable
    public static NbtCompound getBlockEntityNbt(IglooItemStack stack) {
        return stack.getSubNbt("BlockEntityTag");
    }

    public static void setBlockEntityNbt(ItemStack stack, IglooBlockEntityType<?> blockEntityType, NbtCompound tag) {
        setBlockEntityNbt(IglooItemStack.of(stack), blockEntityType, tag);
    }

    public static void setBlockEntityNbt(IglooItemStack stack, IglooBlockEntityType<?> blockEntityType, NbtCompound tag) {
        if (tag.isEmpty()) {
            stack.removeSubNbt("BlockEntityTag");
        } else {
            IglooBlockEntity.writeIdToNbt(tag, blockEntityType);
            stack.setSubNbt("BlockEntityTag", tag);
        }
    }
}
