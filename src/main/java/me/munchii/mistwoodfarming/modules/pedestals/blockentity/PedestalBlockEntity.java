package me.munchii.mistwoodfarming.modules.pedestals.blockentity;

import me.munchii.igloolib.block.BlockEntityManager;
import me.munchii.igloolib.block.IglooBlockEntity;
import me.munchii.igloolib.gui.toast.Toast;
import me.munchii.igloolib.gui.toast.ToastStyle;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.text.Text;
import me.munchii.igloolib.util.Duration;
import me.munchii.igloolib.util.Logger;
import me.munchii.igloolib.util.StringUtil;
import me.munchii.igloolib.util.TimeUnit;
import me.munchii.mistwoodfarming.RegistryManager;
import me.munchii.mistwoodfarming.modules.wid.api.WIDInformable;
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

import java.util.Locale;
import java.util.Objects;

public class PedestalBlockEntity extends IglooBlockEntity implements WIDInformable {
    public static final int MAX_STORED = 64;

    private GenerationType generationType = null;
    private int stored = 0;
    private int ticks = 0;

    public PedestalBlockEntity(Location pos) {
        super(RegistryManager.BlockEntities.PEDESTAL, pos);
    }

    @Override
    public void tick(World world, Location pos, Block block) {
        super.tick(world, pos, block);

        if (stored >= MAX_STORED) return;

        ticks++;
        Location loc = pos.clone().subtract(0, 1, 0);
        Block beneath = world.getBlockAt(loc);

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

    @Override
    public Text getInformation() {
        if (generationType != null) {
            return new Text.Literal(stored + "x " + StringUtil.toTitleCase(generationType.name().toLowerCase(Locale.ROOT).replace("_", " ")));
        } else {
            return Text.literal(null);
        }
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

            if (event.getClickedBlock() == null) return;
            if (!BlockEntityManager.isBlockEntityAt(event.getClickedBlock().getLocation())) return;
            if (BlockEntityManager.getBlockEntity(event.getClickedBlock().getLocation()).getType() != RegistryManager.BlockEntities.PEDESTAL) return;
            PedestalBlockEntity pedestal = BlockEntityManager.getBlockEntityAt(event.getClickedBlock().getLocation());
            if (pedestal == null) return;

            // left click = take all
            // right click = take one
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                player.getInventory().addItem(pedestal.clearStored());
                player.updateInventory();
                return;
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                player.getInventory().addItem(pedestal.takeOne());
                player.updateInventory();
                return;
            }
        }
    }
}
