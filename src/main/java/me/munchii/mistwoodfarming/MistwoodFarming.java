package me.munchii.mistwoodfarming;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.config.Configuration;
import me.munchii.igloolib.module.ModuleManager;
import me.munchii.igloolib.util.Logger;
import me.munchii.igloolib.util.UUIDCache;
import me.munchii.mistwoodfarming.config.MistwoodFarmingConfig;
import me.munchii.mistwoodfarming.model.FarmData;
import me.munchii.mistwoodfarming.model.PlayerData;
import me.munchii.mistwoodfarming.modules.farming.FarmingModule;
import me.munchii.mistwoodfarming.modules.shop.ShopModule;
import me.munchii.mistwoodfarming.modules.wid.WIDModule;
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

        // TODO: how to include default resource files like en_us.json?

        if (!setupIgloolib()) {
            Logger.severe("Igloolib is not loaded. Disabling FarmingPlugin");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // TODO: fails to load?
        if (!setupEconomy()) {
            Logger.severe("Vault is not loaded. Disabling FarmingPlugin");
            //Bukkit.getPluginManager().disablePlugin(this);
        }

        // TODO: fails to load?
        if (!setupRedProtect()) {
            Logger.severe("RedProtect is not loaded. Disabling FarmingPlugin");
            //Bukkit.getPluginManager().disablePlugin(this);
        }

        new Configuration(MistwoodFarmingConfig.class, this);

        moduleManager = new ModuleManager();
        registerModules();

        setupListeners();

        RegistryManager.register();

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
        moduleManager.registerModule(WIDModule::new);
    }

    private void setupListeners() {
        new EventListener();
    }

    private boolean setupIgloolib() {
        return Bukkit.getPluginManager().getPlugin("igloolib") != null
                && Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("igloolib")).isEnabled()
                && Igloolib.INSTANCE != null;
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
