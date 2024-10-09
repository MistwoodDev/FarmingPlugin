package me.munchii.igloolib.block;

import me.munchii.igloolib.block.entity.IglooBlockEntity;
import me.munchii.igloolib.block.entity.IglooBlockEntityType;
import me.munchii.igloolib.item.IglooItem;
import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.text.Text;
import me.munchii.igloolib.util.KeyUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IglooBlockItem extends IglooItem {
    private static final Map<IglooBlock, IglooBlockItem> BLOCK_ITEMS = new HashMap<>();

    public static IglooBlockItem getBlockItem(IglooBlock block) {
        return BLOCK_ITEMS.computeIfAbsent(block, IglooBlockItem::new);
    }

    private final IglooBlock block;

    public IglooBlockItem(IglooBlock block) {
        this.block = block;
    }

    public IglooBlock getBlock() {
        return block;
    }

    public ItemStack getItem() {
        // TODO: apply nbt
        //return new ItemStack(block.getType(), 1);

        NamespacedKey registryKey = Objects.requireNonNull(IglooRegistry.BLOCK.getId(block));

        ItemStack stack = new ItemStack(block.getType(), 1);

        IglooItemStack item = IglooItemStack.of(stack);
        NbtCompound nbt = item.getOrCreateNbt();
        nbt.putString("IglooBlock", registryKey.toString());
        item.setNbt(nbt);
        stack = item.asBukkitStack();

        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            String key = KeyUtil.toDottedString(KeyUtil.join("block", registryKey));
            meta.setDisplayName(Text.translatableColor(null, key).toString());
            stack.setItemMeta(meta);
        }

        return stack;
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
