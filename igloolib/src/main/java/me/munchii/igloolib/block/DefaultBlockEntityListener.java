package me.munchii.igloolib.block;

import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.registry.IglooRegistry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DefaultBlockEntityListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // TODO: handle interaction (like screens or something else)
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack stack = event.getItemInHand();
        IglooItemStack item = IglooItemStack.of(stack);
        NbtCompound nbt = item.getOrCreateNbt();
        if (nbt.contains("IglooBlock")) {
            NamespacedKey registryKey = NamespacedKey.fromString(nbt.getString("IglooBlock"));
            if (registryKey == null) return;

            IglooBlockEntityType<?> blockEntityType = IglooRegistry.BLOCK_ENTITY_TYPE.get(registryKey);
            if (blockEntityType == null) return;

            IglooBlockEntity blockEntity = blockEntityType.instantiate(event.getBlock().getLocation());
            if (blockEntity == null) return;

            BlockEntityManager.addBlockEntity(event.getBlock().getLocation(), blockEntityType, blockEntity);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (BlockEntityManager.isBlockEntityAt(event.getBlock().getLocation())) {
            event.setDropItems(false);
            IglooBlockEntity blockEntity = BlockEntityManager.removeBlockEntity(event.getBlock().getLocation());

            //ItemStack dropStack = blockEntity.getDrop(event.getPlayer());
            ItemStack dropStack = new ItemStack(Material.AIR);
            // TODO: how do we get the drop?
            // TODO: blockEntity.getDrop(p) -> problems: it uses the blockpos to determine material (is it even there still), it doesn't set displayName and NBT
            // TODO: everything else -> problems: what is the material? and how do we figure out the displayName and NBT
            // TODO: we could get the IglooBlock, but how?

            event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), blockEntity.getDrop(event.getPlayer()));
        }
    }
}
