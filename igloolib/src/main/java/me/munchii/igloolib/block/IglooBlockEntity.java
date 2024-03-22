package me.munchii.igloolib.block;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class IglooBlockEntity {
    private final IglooBlockEntityType<?> blockEntityType;
    private final Location pos;

    public IglooBlockEntity(IglooBlockEntityType<?> blockEntityType, Location pos) {
        this.blockEntityType = blockEntityType;
        this.pos = pos;
    }

    public void tick(World world, Location pos, org.bukkit.block.Block block) {

    }

    @NotNull
    public Map<String, String> writeChunkData() {
        return Map.of();
    }

    public void loadChunkData(@NotNull Map<String, String> data) {

    }

    public final IglooBlockEntityType<?> getType() {
        return blockEntityType;
    }

    public final Location getPos() {
        return pos;
    }
}
