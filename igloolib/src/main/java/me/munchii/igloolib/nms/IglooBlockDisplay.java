package me.munchii.igloolib.nms;

import me.munchii.igloolib.util.Logger;
import net.minecraft.world.entity.Display;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftDisplay;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;

public class IglooBlockDisplay extends CraftDisplay implements BlockDisplay {
    public IglooBlockDisplay(CraftServer server, Display.BlockDisplay entity) {
        super(server, entity);
    }

    @Override
    public void remove() {
        super.remove();
        Logger.severe("custom remove");
    }

    public Display.BlockDisplay getHandle() {
        return (Display.BlockDisplay) super.getHandle();
    }

    @NotNull
    @Override
    public BlockData getBlock() {
        return CraftBlockData.fromData(this.getHandle().t());
    }

    @Override
    public void setBlock(@NotNull BlockData blockData) {
        assert blockData != null;
        this.getHandle().c(((CraftBlockData) blockData).getState());
    }
}
