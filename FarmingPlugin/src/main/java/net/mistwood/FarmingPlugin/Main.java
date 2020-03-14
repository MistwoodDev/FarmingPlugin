package net.mistwood.FarmingPlugin;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Data.PlayerData;
import net.mistwood.FarmingPlugin.Modules.Shop.ShopModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin
{

    private static Main Instance;

    protected Config Config;
    protected Economy Economy;
    protected Cache<PlayerData> PlayersCache;
    protected Cache<FarmData> FarmsCache;

    protected List<Module> Modules;
    protected List<EventManager> EventManagers;

    @Override
    public void onEnable ()
    {
        Instance = this;
        PlayersCache =  new Cache<PlayerData> ();
        FarmsCache =  new Cache<FarmData> ();
        Modules = new ArrayList<Module> ();
        EventManagers = new ArrayList<EventManager> ();

        Modules.add (new ShopModule ());
        for (Module Module : Modules)
            Module.OnEnable (this);

        // TODO: Load commands (Only default commands, as each module loads their own commands)

        new EventListener (this);

        if (!SetupConfig ())
            Bukkit.getLogger ().warning ("[Farming - 1.0.0] Failed to load config");

        if (!SetupEconomy ())
            Bukkit.getLogger ().warning ("[Farming - 1.0.0] Failed to initialize economy");

        // TODO: Connect to DB

        Bukkit.getLogger ().info ("[Farming - 1.0.0] Enabled");
    }

    @Override
    public void onDisable ()
    {
        for (Module Module : Modules)
            Module.OnDisable ();

        Bukkit.getLogger ().info ("[Farming - 1.0.0] Disabled");
    }

    public void RegisterEventManager (EventManager Manager)
    {
        EventManagers.add (Manager);
    }

    public void RegisterCommand (String Name, CommandExecutor Command)
    {
        getCommand (Name).setExecutor (Command);
    }

    private boolean SetupConfig ()
    {
        File ConfigFile = new File (getDataFolder (), "config.yml");
        if (!ConfigFile.exists ())
        {
            ConfigFile.getParentFile ().mkdirs ();
            saveResource ("config.yml", false);
        }

        FileConfiguration FileConfig = new YamlConfiguration ();
        try
        {
            FileConfig.load (ConfigFile);
            Config = new Config (FileConfig);
        }

        catch (IOException | InvalidConfigurationException Error)
        {
            Error.printStackTrace ();

            return false;
        }

        return true;
    }

    private boolean SetupEconomy ()
    {
        RegisteredServiceProvider<Economy> EconomyProvider = getServer ().getServicesManager ().getRegistration (net.milkbowl.vault.economy.Economy.class);

        if (EconomyProvider != null)
            Economy = EconomyProvider.getProvider ();

        return (Economy != null);
    }

}
