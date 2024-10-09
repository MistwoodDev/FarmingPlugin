package me.munchii.igloolib.screen;

import me.munchii.igloolib.block.entity.BlockEntityManager;
import me.munchii.igloolib.block.entity.IglooBlockEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ScreenListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) return;
        if (!BlockEntityManager.isBlockEntityAt(event.getClickedBlock().getLocation())) return;
        IglooBlockEntity blockEntity = BlockEntityManager.getBlockEntityAt(event.getClickedBlock().getLocation());
        if (blockEntity == null) return;
        if (!(blockEntity instanceof ScreenProvider screenProvider)) return;

        // left click = ?
        // right click = open screen / inventory
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Screen screen = screenProvider.getScreen();
        }
    }
}
