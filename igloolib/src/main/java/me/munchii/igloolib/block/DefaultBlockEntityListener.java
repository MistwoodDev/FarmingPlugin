package me.munchii.igloolib.block;

import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.text.Text;
import me.munchii.igloolib.util.KeyUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

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

            BlockEntityManager.addBlockEntity(event.getBlock().getLocation(), blockEntity);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (BlockEntityManager.isBlockEntityAt(event.getBlock().getLocation())) {
            // TODO: needs testing

            event.setDropItems(false);

            IglooBlockEntity blockEntity = BlockEntityManager.removeBlockEntity(event.getBlock().getLocation());
            IglooBlockEntityType<?> blockEntityType = blockEntity.getType();

            NamespacedKey registryKey = Objects.requireNonNull(IglooRegistry.BLOCK_ENTITY_TYPE.getId(blockEntityType));

            ItemStack stack = new ItemStack(event.getBlock().getType(), 1);

            IglooItemStack item = IglooItemStack.of(stack);
            NbtCompound nbt = item.getOrCreateNbt();
            nbt.putString("IglooBlock", registryKey.toString());
            item.setNbt(nbt);
            stack = item.asBukkitStack();

            ItemMeta meta = stack.getItemMeta();
            if (meta != null) {
                String key = KeyUtil.toDottedString(KeyUtil.join("block", registryKey));
                meta.setDisplayName(Text.translatableColor(event.getPlayer(), key).toString());
                stack.setItemMeta(meta);
            }

            event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), stack);
        }
    }
}
