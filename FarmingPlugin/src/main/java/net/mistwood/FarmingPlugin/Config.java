package net.mistwood.FarmingPlugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Config
{

    public String DatabaseHost;
    public int DatabasePort;
    public String DatabaseName;
    public String DatabaseUsername;
    public String DatabasePassword;
    public String DatabasePlayersCollection;
    public String DatabaseFarmsCollection;
    public String DatabaseAuthKeysCollection;

    public boolean ModulesDiscordLink;
    
    public boolean PlaySounds;

    // TODO: Add more

    public Config (FileConfiguration FileConfig)
    {
        ConfigurationSection DatabaseSection = FileConfig.getConfigurationSection ("Database");
        assert DatabaseSection != null;
        this.DatabaseHost = Objects.requireNonNull (DatabaseSection.get ("Host")).toString ();
        this.DatabasePort = Integer.parseInt (Objects.requireNonNull (DatabaseSection.get ("Port")).toString ());
        this.DatabaseName = Objects.requireNonNull (DatabaseSection.get ("Name")).toString ();
        this.DatabaseUsername = Objects.requireNonNull (DatabaseSection.get ("Username")).toString ();
        this.DatabasePassword = Objects.requireNonNull (DatabaseSection.get ("Password")).toString ();
        this.DatabasePlayersCollection = Objects.requireNonNull (DatabaseSection.get ("PlayersCollection")).toString ();
        this.DatabaseFarmsCollection = Objects.requireNonNull (DatabaseSection.get ("FarmsCollection")).toString ();
        this.DatabaseAuthKeysCollection = Objects.requireNonNull (DatabaseSection.get ("AuthKeysCollection")).toString ();

        ConfigurationSection ModulesSection = FileConfig.getConfigurationSection ("Modules");
        assert ModulesSection != null;
        this.ModulesDiscordLink = Boolean.getBoolean (Objects.requireNonNull (ModulesSection.get ("DiscordLink")).toString ());
        
        ConfigurationSection FishingSection = FileConfig.getConfigurationSection ("Fishing");
        assert FishingSection != null;
        this.PlaySounds = Objects.requireNonNull (FishingSection.get ("PlaySounds")).toString ().equals ("true");
    }

}
