package me.munchii.igloolib.block;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TileEntityTypes;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class IglooBlockEntity {
    private final NMSBlockEntity tileEntity;
    private final Location pos;

    public IglooBlockEntity(Location pos) {
        // TODO: is tileEntity needed really?
        // TODO: probably to store data, but do we need it. and if so, how would that work?
        // TODO: nvm, yeah. because how else would we know when we destroy it
        this.tileEntity = new NMSBlockEntity(TileEntityTypes.t, new BlockPosition(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), Block.a(425));
        this.pos = pos;
    }

    public abstract void tick(World world, Location pos, org.bukkit.block.Block block);

    @NotNull
    public ItemStack getDrop(Player player) {
        // TODO: is there a better way than this?
        return new ItemStack(Objects.requireNonNull(pos.getWorld()).getBlockAt(pos).getType(), 1);
    }

    @NotNull
    public final NMSBlockEntity getTileEntity() {
        return tileEntity;
    }
}
