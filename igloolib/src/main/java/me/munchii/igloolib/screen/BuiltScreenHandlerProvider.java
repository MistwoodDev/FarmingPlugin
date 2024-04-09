package me.munchii.igloolib.screen;

import me.munchii.igloolib.player.IglooPlayer;

public interface BuiltScreenHandlerProvider extends ScreenProvider {
    BuiltScreenHandler createScreenHandler(IglooPlayer player);
}
