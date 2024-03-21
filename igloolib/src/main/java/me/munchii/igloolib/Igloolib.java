package me.munchii.igloolib;

import me.munchii.igloolib.config.Configuration;
import me.munchii.igloolib.util.ListenerManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Supplier;

public final class Igloolib extends JavaPlugin {

    public static Igloolib INSTANCE = null;
    private final static ListenerManager LISTENERS = new ListenerManager();

    @Override
    public void onEnable() {
        INSTANCE = this;

        new Configuration(IgloolibConfig.class, this);

        getLogger().info("igloolib initialized");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Listener registerListener(Supplier<Listener> listenerSupplier) {
        return LISTENERS.register(listenerSupplier);
    }
}
