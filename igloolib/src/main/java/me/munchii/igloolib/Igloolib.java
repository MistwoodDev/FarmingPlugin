package me.munchii.igloolib;

import org.bukkit.plugin.java.JavaPlugin;

public final class Igloolib extends JavaPlugin {

    public static Igloolib INSTANCE = null;

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
