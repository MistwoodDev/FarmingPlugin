package me.munchii.mistwoodfarming.modules.farmerscrafting.blockentity;

import me.munchii.igloolib.block.IglooBlockEntity;
import me.munchii.mistwoodfarming.RegistryManager;
import org.bukkit.Location;

public class FarmersCraftingTableBlockEntity extends IglooBlockEntity {
    public FarmersCraftingTableBlockEntity(Location pos) {
        super(RegistryManager.BlockEntities.FARMERS_CRAFTING_TABLE, pos);
    }
}
