package me.munchii.mistwoodfarming.modules.shop.blockentity;

import me.munchii.igloolib.block.IglooBlockEntity;
import me.munchii.igloolib.util.Logger;
import me.munchii.mistwoodfarming.RegistryManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ShopBlockEntity extends IglooBlockEntity {
    private int progress = 0;
    private int alive = 0;

    public ShopBlockEntity(Location pos) {
        super(RegistryManager.BlockEntities.SHOP_BLOCK, pos);
        Logger.severe("AAABBB ShopBlockEntity Init");
    }

    @Override
    public void tick(World world, Location pos, Block block) {
        // every 2 sec
        if (progress >= 40) {
            alive++;
            Logger.severe("AAABBB ShopBlockEntity alive=" + alive);
            progress = 0;
        } else {
            progress++;
        }
    }

    @Override
    public @NotNull Map<String, String> writeChunkData() {
        return Map.of("progress", String.valueOf(progress), "alive", String.valueOf(alive));
    }

    @Override
    public void loadChunkData(@NotNull Map<String, String> data) {
        progress = Integer.parseInt(data.get("progress"));
        alive = Integer.parseInt(data.get("alive"));
    }
}
