package net.mistwood.FarmingPlugin.Utils;

public class Messages
{

    // Prefixes
    public static String ConsolePrefix = "[Farming - 1.0.3] ";
    public static String ChatPrefix = "&7[&bFarming&7] ";
    public static String ChatErrorPrefix = ChatPrefix + "&b&lError ";

    // Console messages
    public static String PluginEnabled = ConsolePrefix + "Enabled";
    public static String PluginDisabled = ConsolePrefix + "Disabled";
    public static String PluginDatabaseConnected = ConsolePrefix + "Successfully connected to database!";
    public static String PluginModuleLoaded = ConsolePrefix + "Module '%s' was successfully loaded!";
    public static String PluginLoadedConfig = ConsolePrefix + "Successfully loaded config!";
    public static String PluginLoadedEconomy = ConsolePrefix + "Successfully loaded economy!";
    public static String PluginConfigFailed = ConsolePrefix + "Failed to load config!";
    public static String PluginEconomyFailed = ConsolePrefix + "Failed to initialize economy!";
    public static String PluginRedProtectFailed = ConsolePrefix + "RedProtect plugin could not be found or is disabled!";

    // Errors
    public static String NoCommandPermission = ChatErrorPrefix + "&7You don't have the required permission to perform that!";
    public static String PlayerNotInFarm = ChatErrorPrefix + "&b%s &7not currently in a farm!";
    public static String YouNotInFarm = ChatErrorPrefix + "&7You're not currently in a farm!";
    public static String PlayerAlreadyInFarm = ChatErrorPrefix + "&b%s &7not currently in a farm!";
    public static String YouAlreadyInFarm = ChatErrorPrefix + "&7You're not currently in a farm!";
    public static String PlayerNotFound = ChatErrorPrefix + "&7Player &b'%s&b' &7not found!";

    public static String FarmCreated = ChatPrefix + "&7Created new farm with name &b%s&7!";

}
