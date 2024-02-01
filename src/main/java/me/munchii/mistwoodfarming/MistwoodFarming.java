package me.munchii.mistwoodfarming;

import me.munchii.igloolib.config.Configuration;
import me.munchii.igloolib.module.ModuleManager;
import me.munchii.igloolib.util.UUIDCache;
import me.munchii.mistwoodfarming.config.MistwoodFarmingConfig;
import me.munchii.mistwoodfarming.model.FarmData;
import me.munchii.mistwoodfarming.model.PlayerData;
import me.munchii.mistwoodfarming.modules.farming.FarmingModule;
import me.munchii.mistwoodfarming.modules.shop.ShopModule;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MistwoodFarming extends JavaPlugin {
    public static MistwoodFarming INSTANCE;

    private ModuleManager moduleManager;

    private UUIDCache<PlayerData> playerCache;
    private UUIDCache<FarmData> farmCache;

    private Economy economyService;

    @Override
    public void onEnable() {
        INSTANCE = this;

        new Configuration(MistwoodFarmingConfig.class, this);

        moduleManager = new ModuleManager();
        registerModules();

        playerCache = new UUIDCache<>(1024);
        farmCache = new UUIDCache<>(1024);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerModules() {
        moduleManager.registerModule(FarmingModule::new);
        moduleManager.registerModule(ShopModule::new);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer()
                .getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);

        if (economyProvider != null) {
            economyService = economyProvider.getProvider();
        }

        return economyService != null;
    }

    private boolean setupRedProtect() {
        return Bukkit.getPluginManager().getPlugin("RedProtect") != null
                && Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("RedProtect")).isEnabled();
    }

    public Economy getEconomyService() {
        return economyService;
    }

    public UUIDCache<PlayerData> getPlayerCache() {
        return playerCache;
    }

    public UUIDCache<FarmData> getFarmCache() {
        return farmCache;
    }
}
