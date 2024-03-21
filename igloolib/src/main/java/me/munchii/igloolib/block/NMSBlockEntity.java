package me.munchii.igloolib.block;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityTypes;
import net.minecraft.world.level.block.state.IBlockData;
import org.jetbrains.annotations.NotNull;

public class NMSBlockEntity extends TileEntity {
    public NMSBlockEntity(@NotNull TileEntityTypes<?> type, @NotNull BlockPosition pos, @NotNull IBlockData data) {
        super(type, pos, data);
    }
}
