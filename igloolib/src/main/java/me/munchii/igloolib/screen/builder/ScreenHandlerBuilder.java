package me.munchii.igloolib.screen.builder;

import me.munchii.igloolib.gui.inventory.slot.Slot;
import me.munchii.igloolib.player.IglooPlayer;
import me.munchii.igloolib.screen.BuiltScreenHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ScreenHandlerBuilder {
    private final String name;

    final List<Slot> slots;

    public ScreenHandlerBuilder(final String name) {
        this.name = name;

        this.slots = new ArrayList<>();
    }

    private Predicate<IglooPlayer> isUsable() {
        return player -> true;
    }

    public BuiltScreenHandler create() {
        final BuiltScreenHandler built = new BuiltScreenHandler(name, isUsable());


        slots.clear();
        return built;
    }
}
