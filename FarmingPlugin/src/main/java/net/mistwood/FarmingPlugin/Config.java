package net.mistwood.FarmingPlugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config
{

    public String DatabaseHost;
    public int DatabasePort;
    public String DatabaseName;
    public String DatabaseUsername;
    public String DatabasePassword;
    public String DatabasePlayersCollection;
    public String DatabaseFarmsCollection;

    // TODO: Add more

    public Config (FileConfiguration FileConfig)
    {
        ConfigurationSection DatabaseSection = FileConfig.getConfigurationSection ("Database");
        this.DatabaseHost = DatabaseSection.get ("Host").toString ();
        this.DatabasePort = Integer.parseInt (DatabaseSection.get ("Port").toString ());
        this.DatabaseName = DatabaseSection.get ("Name").toString ();
        this.DatabaseUsername = DatabaseSection.get ("Username").toString ();
        this.DatabasePassword = DatabaseSection.get ("Password").toString ();
        this.DatabasePlayersCollection = DatabaseSection.get ("PlayersCollection").toString ();
        this.DatabaseFarmsCollection = DatabaseSection.get ("FarmsCollection").toString ();
    }

}
