package net.mistwood.FarmingPlugin;

import net.milkbowl.vault.economy.Economy;

import net.mistwood.FarmingPlugin.Database.DatabaseManager;
import net.mistwood.FarmingPlugin.Modules.Farm.FarmModule;
import net.mistwood.FarmingPlugin.Utils.Messages;
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

    public DatabaseManager Database;
    public Config Config;
    public Economy Economy;
    public Cache<PlayerData> PlayersCache;
    public Cache<FarmData> FarmsCache;
    public List<Module> Modules;

    @Override
    public void onEnable ()
    {
        Instance = this;
        PlayersCache =  new Cache<PlayerData> ();
        FarmsCache =  new Cache<FarmData> ();
        Modules = new ArrayList<Module> ();

        Modules.add (new ShopModule ());
        Modules.add (new FarmModule ());
        for (Module Module : Modules)
            Module.OnEnable (this);

        // TODO: Load commands (Only default commands, as each module loads their own commands)

        new EventListener (this);

        if (!SetupConfig ())
            Bukkit.getLogger ().severe (Messages.PluginConfigFailed);

        if (!SetupEconomy ())
            Bukkit.getLogger ().severe (Messages.PluginEconomyFailed);

        if (!SetupRedProtect ())
            Bukkit.getLogger ().severe (Messages.PluginRedProtectFailed);

        Database = new DatabaseManager (Config);
        Database.Connect ();
        Bukkit.getLogger ().info (Messages.PluginDatabaseConnected);

        Bukkit.getLogger ().info (Messages.PluginEnabled);
    }

    @Override
    public void onDisable ()
    {
        for (Module Module : Modules)
            Module.OnDisable ();

        Bukkit.getLogger ().info (Messages.PluginDisabled);
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

        Bukkit.getLogger ().info (Messages.PluginLoadedConfig);

        return true;
    }

    private boolean SetupEconomy ()
    {
        RegisteredServiceProvider<Economy> EconomyProvider = getServer ().getServicesManager ().getRegistration (net.milkbowl.vault.economy.Economy.class);

        if (EconomyProvider != null)
            Economy = EconomyProvider.getProvider ();
            Bukkit.getLogger ().info (Messages.PluginLoadedEconomy);

        return (Economy != null);
    }

    private boolean SetupRedProtect ()
    {
        return Bukkit.getPluginManager ().getPlugin ("RedProtect") != null && Bukkit.getPluginManager ().getPlugin ("RedProtect").isEnabled ();
    }

}
