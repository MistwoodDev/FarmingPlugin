package net.mistwood.FarmingPlugin;

import net.mistwood.FarmingPlugin.Database.DatabaseCollection;
import net.mistwood.FarmingPlugin.Utils.ProfanityFilter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import net.mistwood.FarmingPlugin.API.FarmingAPI;
import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Commands.DefaultCommands.ModulesCommand;
import net.mistwood.FarmingPlugin.Database.DatabaseManager;
import net.mistwood.FarmingPlugin.Modules.Challenge.ChallengeModule;
import net.mistwood.FarmingPlugin.Modules.Farm.FarmModule;
import net.mistwood.FarmingPlugin.Modules.DiscordLink.DiscordLinkModule;
import net.mistwood.FarmingPlugin.Utils.Messages;
import net.mistwood.FarmingPlugin.Utils.PermissionManager;
import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Modules.Shop.ShopModule;
import net.mistwood.FarmingPlugin.Modules.Fishing.FishingModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class FarmingPlugin extends JavaPlugin {

    // TODO: Save to DB now and then

    public static FarmingPlugin instance;

    // TODO: Make some of these private and make getters for them
    public DatabaseManager database;
    public Config config;
    public Economy economy;
    public Cache<PlayerData> playersCache;
    public Cache<FarmData> farmsCache;
    public List<Module> modules;
    public PermissionManager permissionManager;
    public ProfanityFilter filter;

    private FarmingAPI api;
    private CommandHandler defaultCommandHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.playersCache = new Cache<>();
        this.farmsCache = new Cache<>();
        this.modules = new ArrayList<>();
        this.api = new FarmingAPI();
        this.permissionManager = new PermissionManager();
        this.filter = new ProfanityFilter();

        this.defaultCommandHandler = new CommandHandler("farming");
        defaultCommandHandler.registerCommand(asList("modules"), new ModulesCommand());

        new EventListener();

        if (!setupConfig())
            Bukkit.getLogger().severe(Messages.PLUGIN_CONFIG_LOAD_FAILED);

        if (!setupEconomy())
            Bukkit.getLogger().severe(Messages.PLUGIN_ECONOMY_LOAD_FAILED);

        if (!setupRedProtect())
            Bukkit.getLogger().severe(Messages.PLUGIN_REDPROTECT_LOAD_FAILED);

        loadModules();
        connectDatabase();
        startDatabaseSaveTask();

        Bukkit.getLogger().info(Messages.PLUGIN_ENABLED);
    }

    @Override
    public void onDisable() {
        for (Module module : modules)
            module.onDisable();

        Bukkit.getLogger().info(Messages.PLUGIN_DISABLED);
    }

    public void registerCommand(String name, CommandExecutor cmd) {
        Objects.requireNonNull(getCommand(name)).setExecutor(cmd);
    }

    private boolean setupConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        FileConfiguration fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(configFile);
            this.config = new Config(fileConfiguration);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }

        Bukkit.getLogger().info(Messages.PLUGIN_CONFIG_LOADED);
        return true;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            this.economy = economyProvider.getProvider();
            Bukkit.getLogger().info(Messages.PLUGIN_ECONOMY_LOADED);
        }

        return economy != null;
    }

    private boolean setupRedProtect() {
        return Bukkit.getPluginManager().getPlugin("RedProtect") != null && Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("RedProtect")).isEnabled();
    }

    private void loadModules() {
        // Default Modules
        modules.add(new ShopModule());
        modules.add(new FarmModule());
        modules.add(new FishingModule());
        modules.add(new ChallengeModule());

        // Configurable Modules
        if (config.modulesConfig.discordLink)
            modules.add(new DiscordLinkModule());

        for (Module module : modules) {
            try {
                module.onEnable();
                Bukkit.getLogger().info(String.format(Messages.PLUGIN_MODULE_LOADED, module.getName()));
            } catch (Exception e) {
                Bukkit.getLogger().severe(String.format(Messages.PLUGIN_MODULE_FAILED, module.getName(), e.getClass().getName()));
            }
        }
    }

    private void connectDatabase() {
        this.database = new DatabaseManager(config);
        this.database.connect();
        Bukkit.getLogger().info(Messages.PLUGIN_DATABASE_CONNECTED);
    }

    private void startDatabaseSaveTask() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (PlayerData player : playersCache.getAll()) {
                    database.update(player.playerInstance.getUniqueId(), player.toMap(), DatabaseCollection.PLAYERS);
                }

                for (FarmData farm : farmsCache.getAll()) {
                    database.update(farm.id, farm.toMap(), DatabaseCollection.FARMS);
                }
            }
        }, 18000L, 18000L);
    }

    public FarmingAPI getAPI() {
        return api;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().toLowerCase().equals(name.toLowerCase()))
                return module;
        }
        return null;
    }

}
