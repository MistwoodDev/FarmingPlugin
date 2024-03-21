package me.munchii.igloolib.block;

import me.munchii.igloolib.item.IglooItem;
import org.bukkit.inventory.ItemStack;

public class IglooBlockItem extends IglooItem {
    private final IglooBlock block;

    public IglooBlockItem(IglooBlock block) {
        this.block = block;
    }

    public ItemStack getItem() {
        // TODO: apply nbt
        return new ItemStack(block.getType(), 1);
    }
}
