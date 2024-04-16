package me.munchii.igloolib.screen;

import me.munchii.igloolib.gui.inventory.IInventoryGUI;
import me.munchii.igloolib.player.IglooPlayer;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class BuiltScreenHandler {
    private final String name;
    private final Predicate<IglooPlayer> canInteract;

    IInventoryGUI window;

    public BuiltScreenHandler(final String name, final Predicate<IglooPlayer> canInteract) {
        this.name = name;
        this.canInteract = canInteract;
    }

    public boolean canUse(final Player player) {
        return canInteract.test(IglooPlayer.get(player));
    }
}
