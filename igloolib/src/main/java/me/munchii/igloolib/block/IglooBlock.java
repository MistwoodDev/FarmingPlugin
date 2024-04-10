package me.munchii.igloolib.block;

import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.text.Text;
import me.munchii.igloolib.util.KeyUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IglooBlock {
    private final Material type;

    protected IglooBlock(@NotNull Material type) {
        this.type = type;
    }

    public static IglooBlock of(@NotNull Material type) {
        return new IglooBlock(type);
    }

    @NotNull
    public Material getType() {
        return type;
    }

    @NotNull
    public ItemStack getDefaultStack(Player player) {
        return getDefaultStack(player, 1);
    }

    @NotNull
    public ItemStack getDefaultStack(Player player, int count) {
        NamespacedKey registryKey = Objects.requireNonNull(IglooRegistry.BLOCK.getId(this));

        IglooBlockItem blockItem = asItem();
        //ItemStack stack = new ItemStack(type, count);
        ItemStack stack = blockItem.getItem();
        stack.setAmount(count);

        IglooItemStack item = IglooItemStack.of(stack);
        NbtCompound nbt = item.getOrCreateNbt();
        nbt.putString("IglooBlock", registryKey.toString());
        item.setNbt(nbt);
        stack = item.asBukkitStack();

        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            String key = KeyUtil.toDottedString(KeyUtil.join("block", registryKey));
            meta.setDisplayName(Text.translatableColor(player, key).toString());
            stack.setItemMeta(meta);
        }

        return stack;
    }

    @NotNull
    public IglooBlockItem asItem() {
        return IglooBlockItem.getBlockItem(this);
    }
}
