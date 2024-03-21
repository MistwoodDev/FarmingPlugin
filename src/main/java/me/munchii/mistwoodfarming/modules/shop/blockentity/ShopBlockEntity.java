package me.munchii.mistwoodfarming.modules.shop.blockentity;

import me.munchii.igloolib.block.IglooBlockEntity;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class ShopBlockEntity extends IglooBlockEntity {
    public ShopBlockEntity(Location pos) {
        super(pos);
        Logger.severe("AAABBB ShopBlockEntity Init");
    }

    @Override
    public void tick(World world, Location pos, Block block) {
        Logger.severe("AAABBB ShopBlockEntity Tick");
    }
}
