package net.mistwood.FarmingPlugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config
{

    protected String DatabaseHost;
    protected int DatabasePort;
    protected String DatabaseName;
    protected String DatabaseUsername;
    protected String DatabasePassword;
    protected String DatabaseUsersTable;
    protected String DatabaseFarmsTable;

    // TODO: Add more

    public Config (FileConfiguration FileConfig)
    {
        ConfigurationSection DatabaseSection = FileConfig.getConfigurationSection ("Database");
        this.DatabaseHost = DatabaseSection.get ("Host").toString ();
        this.DatabasePort = Integer.parseInt (DatabaseSection.get ("Port").toString ());
        this.DatabaseName = DatabaseSection.get ("Name").toString ();
        this.DatabaseUsername = DatabaseSection.get ("Username").toString ();
        this.DatabasePassword = DatabaseSection.get ("Password").toString ();
        this.DatabaseUsersTable = DatabaseSection.get ("UsersTable").toString ();
        this.DatabaseFarmsTable = DatabaseSection.get ("FarmsTable").toString ();
    }

}
