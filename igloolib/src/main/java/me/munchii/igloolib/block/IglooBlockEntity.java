package me.munchii.igloolib.block;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TileEntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class IglooBlockEntity {
    private final NMSBlockEntity tileEntity;
    private Location pos = null;

    /*private IglooBlockEntity(BlockPosition pos) {
        this.tileEntity = new NMSBlockEntity(TileEntityTypes.BANNER, pos, Block.stateById(425));
        getPos();
    }*/

    public IglooBlockEntity(Location pos) {
        //this(new BlockPosition(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()));
        //this.tileEntity = new NMSBlockEntity(TileEntityTypes.BANNER, new BlockPosition(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), Block.stateById(425));
        this.tileEntity = new NMSBlockEntity(TileEntityTypes.t, new BlockPosition(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), Block.a(425));
        //getPos();
        this.pos = pos;
    }

    public abstract void tick(World world, Location pos, org.bukkit.block.Block block);

    @NotNull
    public ItemStack getDrop(Player player) {
        return new ItemStack(Objects.requireNonNull(pos.getWorld()).getBlockAt(pos).getType(), 1);
    }

    @NotNull
    public final NMSBlockEntity getTileEntity() {
        return tileEntity;
    }

    @NotNull
    public final Location getPos() {
        if (pos == null) {
            //BlockPosition blockPosition = tileEntity.getBlockPos();
            BlockPosition blockPosition = tileEntity.p();
            pos = new Location(
                    //Bukkit.getWorld(Objects.requireNonNull(tileEntity.getLevel()).toString()),
                    Bukkit.getWorld(Objects.requireNonNull(tileEntity.k()).toString()),
                    //blockPosition.getX(),
                    blockPosition.b().c,
                    //blockPosition.getY(),
                    blockPosition.b().d,
                    //blockPosition.getZ());
                    blockPosition.b().e);
        }

        return pos;
    }
}
