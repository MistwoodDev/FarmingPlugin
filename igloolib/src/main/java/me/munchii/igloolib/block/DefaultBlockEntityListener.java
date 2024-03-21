package me.munchii.igloolib.block;

import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.util.Logger;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DefaultBlockEntityListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

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
            Logger.severe(blockEntity.getClass().getName());
            blockEntity.tick(null, null, null);
            BlockEntityManager.addBlockEntity(event.getBlock().getLocation(), blockEntity);
        }
    }
}
