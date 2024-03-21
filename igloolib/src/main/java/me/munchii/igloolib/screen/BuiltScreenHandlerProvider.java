package me.munchii.igloolib.screen;

import me.munchii.igloolib.player.IglooPlayer;

public interface BuiltScreenHandlerProvider {
    BuiltScreenHandler createScreenHandler(IglooPlayer player);
}
