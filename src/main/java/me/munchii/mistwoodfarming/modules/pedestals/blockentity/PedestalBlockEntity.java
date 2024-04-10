package me.munchii.mistwoodfarming.modules.pedestals.blockentity;

import me.munchii.igloolib.block.BlockEntityManager;
import me.munchii.igloolib.block.IglooBlockEntity;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.util.Logger;
import me.munchii.mistwoodfarming.RegistryManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PedestalBlockEntity extends IglooBlockEntity {
    public static final int MAX_STORED = 64;

    private GenerationType generationType = null;
    private int stored;
    private int ticks;

    public PedestalBlockEntity(Location pos) {
        super(RegistryManager.BlockEntities.PEDESTAL, pos);
        Logger.severe("AAABBB PedestalBlockEntity Init");
    }

    @Override
    public void tick(World world, Location pos, Block block) {
        super.tick(world, pos, block);

        if (stored >= MAX_STORED) return;

        ticks++;
        Block beneath = world.getBlockAt(pos.subtract(0, 1, 0));

        // if generationType hasn't been set yet - first time it's been placed
        if (generationType == null) {
            generationType = getFromBeneathMaterial(beneath.getType());
            ticks = 0;

            if (generationType == null) return;
        }

        // if the block beneath the pedestal has been changed
        // TODO: right now you can generate 36 cobblestone (with stone beneath) and change it with a grass block and then get 36 dirt. should we keep it like this or change the behaviour to only start generating the new type once the old type has been emptied?
        if (!beneath.getType().equals(generationType.beneathType)) {
            generationType = getFromBeneathMaterial(beneath.getType());
            ticks = 0;

            if (generationType == null) return;
        }

        if (ticks >= generationType.ticks) {
            Logger.severe("AAABBB PedestalBlockEntity Tick5 (Generation)");
            ticks = 0;
            stored++;
        }
    }

    public ItemStack takeOne() {
        if (stored < 1) return new ItemStack(Material.AIR);
        stored--;
        return new ItemStack(generationType.generationType, 1);
    }

    public ItemStack clearStored() {
        ItemStack stack = new ItemStack(generationType.generationType, stored);
        stored = 0;
        return stack;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        stored = nbt.getInt("stored");

        String genType = nbt.getString("generationType");
        generationType = Objects.equals(genType, "null") ? null : GenerationType.valueOf(genType);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("stored", stored);
        nbt.putString("generationType", generationType == null ? "null" : generationType.name());
    }

    @Nullable
    private static GenerationType getFromBeneathMaterial(Material material) {
        for (GenerationType type : GenerationType.values()) {
            if (type.beneathType.equals(material)) {
                return type;
            }
        }

        return null;
    }

    // TODO: make configurable? (add more types of generation)
    // TODO: allow for custom blocks? (IglooBlock)
    public enum GenerationType {
        COBBLESTONE(Material.COBBLESTONE, Material.STONE, 40),
        DIRT(Material.DIRT, Material.GRASS_BLOCK, 40),
        ANDESITE(Material.ANDESITE, Material.POLISHED_ANDESITE, 40),
        DIORITE(Material.DIORITE, Material.POLISHED_DIORITE, 40),
        GRANITE(Material.GRANITE, Material.POLISHED_GRANITE, 40),
        SAND(Material.SAND, Material.SMOOTH_SANDSTONE, 40),
        GRAVEL(Material.GRAVEL, Material.GRAVEL, 40),
        NETHERRACK(Material.NETHERRACK, Material.NETHER_BRICK, 40),
        END_STONE(Material.END_STONE, Material.END_STONE_BRICKS, 40);

        public final Material generationType;
        public final Material beneathType;
        public final int ticks;

        GenerationType(Material generationType, Material beneathType, int ticks) {
            this.generationType = generationType;
            this.beneathType = beneathType;
            this.ticks = ticks;
        }
    }

    public static class PedestalListener implements Listener {
        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player player = event.getPlayer();

            Logger.severe("AAABBB PedestalBlockEntity Click1");

            if (event.getClickedBlock() == null) return;
            Logger.severe("AAABBB PedestalBlockEntity AfterCheck1");
            if (!BlockEntityManager.isBlockEntityAt(event.getClickedBlock().getLocation())) return;
            Logger.severe("AAABBB PedestalBlockEntity AfterCheck2");
            if (BlockEntityManager.getBlockEntity(event.getClickedBlock().getLocation()).getType() != RegistryManager.BlockEntities.PEDESTAL) return;
            Logger.severe("AAABBB PedestalBlockEntity AfterCheck3");
            PedestalBlockEntity pedestal = BlockEntityManager.getBlockEntityAt(event.getClickedBlock().getLocation());
            Logger.severe("AAABBB PedestalBlockEntity AfterCheck4");
            if (pedestal == null) return;

            Logger.severe("AAABBB PedestalBlockEntity Click2");

            // left click = take all
            // right click = take one
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Logger.severe("AAABBB PedestalBlockEntity Click3");
                player.getInventory().addItem(pedestal.clearStored());
                player.updateInventory();
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Logger.severe("AAABBB PedestalBlockEntity Click4");
                player.getInventory().addItem(pedestal.takeOne());
                player.updateInventory();
            }
        }
    }
}
