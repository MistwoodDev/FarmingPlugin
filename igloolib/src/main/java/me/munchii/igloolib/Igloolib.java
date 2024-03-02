package me.munchii.igloolib;

import me.munchii.igloolib.util.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Igloolib extends JavaPlugin {

    public static Igloolib INSTANCE = null;
    public final static ListenerManager LISTENERS = new ListenerManager();

    @Override
    public void onEnable() {
        INSTANCE = this;

        getLogger().info("igloolib initialized");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
