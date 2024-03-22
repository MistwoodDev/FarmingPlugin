import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockCommand;
import net.minecraft.world.level.block.BlockTileEntity;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityBanner;
import net.minecraft.world.level.block.entity.TileEntityBarrel;
import net.minecraft.world.level.block.entity.TileEntityContainer;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockPropertyWood;
import net.minecraft.world.level.block.state.properties.IBlockState;
import net.minecraft.world.level.chunk.Chunk;

public class Test {
    public static void test() {
        NBTTagCompound tag = new NBTTagCompound();
        // in test we can use the "formal" (or whatever you call it) version of NMS (the ones with real names instead of obfuscated names
        // (it doesn't work in "release" (when you run on MC if you use the de-obfuscated names)
        // TODO: we should probably create modules for each NMS version and an API interface for NMS so it's easy to upgrade
        TileEntity tileEntity = new TileEntityBanner(null, null);
        TileEntityBarrel tileEntityContainer = new TileEntityBarrel(null, null);
        Block block = new Block(null);
        Chunk chunk = new Chunk(null, null);
        EntityLiving entityLiving = new EntityBat(null, null);
    }
}
