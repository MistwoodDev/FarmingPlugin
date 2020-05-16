package net.mistwood.FarmingPlugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Config {

    public static class DatabaseConfig {
        public String host;
        public int port;
        public String name;
        public String username;
        public String password;
        public String playersCollection;
        public String farmsCollection;
        public String authKeysCollection;
    }

    public static class ModulesConfig {
        public boolean discordLink;
    }

    public static class FishingConfig {
        public boolean playSounds;
    }

    public DatabaseConfig databaseConfig;
    public ModulesConfig modulesConfig;
    public FishingConfig fishingConfig;

    public Config(FileConfiguration config) {
        ConfigurationSection database = config.getConfigurationSection("Database");
        assert database != null;
        this.databaseConfig.host = Objects.requireNonNull(database.get("Host")).toString();
        this.databaseConfig.port = Integer.parseInt(Objects.requireNonNull(database.get("Port")).toString());
        this.databaseConfig.name = Objects.requireNonNull(database.get("Name")).toString();
        this.databaseConfig.username = Objects.requireNonNull(database.get("Username")).toString();
        this.databaseConfig.password = Objects.requireNonNull(database.get("Password")).toString();
        this.databaseConfig.playersCollection = Objects.requireNonNull(database.get("PlayersCollection")).toString();
        this.databaseConfig.farmsCollection = Objects.requireNonNull(database.get("FarmsCollection")).toString();
        this.databaseConfig.authKeysCollection = Objects.requireNonNull(database.get("AuthKeysCollection")).toString();

        ConfigurationSection modules = config.getConfigurationSection("Modules");
        assert modules != null;
        this.modulesConfig.discordLink = Objects.requireNonNull(modules.get("DiscordLink")).toString().equals("true");

        ConfigurationSection fishing = config.getConfigurationSection("Fishing");
        assert fishing != null;
        this.fishingConfig.playSounds = Objects.requireNonNull(fishing.get("PlaySounds")).toString().equals("true");
    }

}
