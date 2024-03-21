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

    /*public class NMSBarrelBlockEntity extends TileEntityBarrel {

        public NMSBarrelBlockEntity(BlockPosition var0, IBlockData var1) {
            super(var0, var1);
        }

        @Override
        public void startOpen(EntityHuman var0) {
            super.startOpen(var0);
            Logger.severe("AAABBB NMSBarrelBlockEntity startOpen");
        }

        @Override
        public void stopOpen(EntityHuman var0) {
            super.stopOpen(var0);
            Logger.severe("AAABBB NMSBarrelBlockEntity stopOpen");
        }
    }*/
}
