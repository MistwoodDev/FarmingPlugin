package me.munchii.igloolib.gui.window;

import me.munchii.igloolib.player.IglooPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface IglooWindow extends InventoryHolder {
    String getDisplayName();

    InventoryView open(@NotNull IglooPlayer player);

    void close(@NotNull IglooPlayer player);

    @ApiStatus.Internal
    void update();

    class IglooWindowListener implements Listener {
        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (event.getInventory().getHolder() instanceof IglooWindow window) {
                window.close(IglooPlayer.get(event.getPlayer()));
            }
        }
    }
}
