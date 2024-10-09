package me.munchii.igloolib.util;

import org.bukkit.Material;

import java.util.Set;

public enum VanillaFuel {
    LAVA_BUCKET(Material.LAVA_BUCKET, 20000),
    COAL_BLOCK(Material.COAL_BLOCK, 16000),
    DRIED_KELP_BLOCK(Material.DRIED_KELP_BLOCK, 4000),
    BLAZE_ROD(Material.BLAZE_ROD, 2400),
    COAL(Material.COAL, 1600),
    CHARCOAL(Material.CHARCOAL, 1600),
    BOAT(Set.of(Material.ACACIA_BOAT, Material.BIRCH_BOAT, Material.CHERRY_BOAT, Material.DARK_OAK_BOAT, Material.JUNGLE_BOAT, Material.MANGROVE_BOAT, Material.OAK_BOAT, Material.SPRUCE_BOAT), 1200),
    CHEST_BOAT(Set.of(Material.ACACIA_CHEST_BOAT, Material.BIRCH_CHEST_BOAT, Material.CHERRY_CHEST_BOAT, Material.DARK_OAK_CHEST_BOAT, Material.JUNGLE_CHEST_BOAT, Material.MANGROVE_CHEST_BOAT, Material.OAK_CHEST_BOAT, Material.SPRUCE_CHEST_BOAT), 1200),
    BAMBOO_MOSAIC(Set.of(Material.BAMBOO_MOSAIC, Material.BAMBOO_MOSAIC_STAIRS), 300),
    CHISELED_BOOKSHELF(Material.CHISELED_BOOKSHELF, 300),
    BAMBOO_BLOCK(Set.of(Material.BAMBOO_BLOCK, Material.STRIPPED_BAMBOO_BLOCK), 300),
    LOG(Set.of(Material.ACACIA_LOG, Material.BIRCH_LOG, Material.CHERRY_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG), 300),
    STRIPPED_LOG(Set.of(Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_CHERRY_LOG, Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_MANGROVE_LOG, Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG), 300),
    WOOD(Set.of(Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.CHERRY_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD), 300),
    STRIPPED_WOOD(Set.of(Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_CHERRY_WOOD, Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_MANGROVE_WOOD, Material.STRIPPED_OAK_WOOD, Material.STRIPPED_SPRUCE_WOOD), 300),
    PLANKS(Set.of(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.CHERRY_PLANKS, Material.DARK_OAK_PLANKS, Material.JUNGLE_PLANKS, Material.MANGROVE_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS), 300),
    SLABS(Set.of(Material.BAMBOO_MOSAIC_SLAB, Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.CHERRY_PLANKS, Material.DARK_OAK_PLANKS, Material.JUNGLE_PLANKS, Material.MANGROVE_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS), 150),
    ;

    private final Set<Material> fuelType;
    private final int burnTime;

    VanillaFuel(final Material fuelType, final int burnTime) {
        this.fuelType = Set.of(fuelType);
        this.burnTime = burnTime;
    }

    VanillaFuel(final Set<Material> fuelType, final int burnTime) {
        this.fuelType = fuelType;
        this.burnTime = burnTime;
    }
}
