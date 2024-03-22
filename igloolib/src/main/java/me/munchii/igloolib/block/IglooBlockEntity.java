package me.munchii.igloolib.block;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TileEntityTypes;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public abstract class IglooBlockEntity {
    private final Location pos;

    public IglooBlockEntity(Location pos) {
        this.pos = pos;
    }

    public abstract void tick(World world, Location pos, org.bukkit.block.Block block);

    @NotNull
    public ItemStack getDrop(Player player) {
        // TODO: is there a better way than this?
        return new ItemStack(Objects.requireNonNull(pos.getWorld()).getBlockAt(pos).getType(), 1);
    }

    @NotNull
    public Map<String, String> writeChunkData() {
        return Map.of();
    }

    public void loadChunkData(@NotNull Map<String, String> data) {

    }
}
