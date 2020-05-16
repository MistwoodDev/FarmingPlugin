package net.mistwood.FarmingPlugin.Utils;

public class Messages
{

    // Prefixes
    public static final String CONSOLE_PREFIX = "[Farming - 1.0.3] ";
    public static final String CHAT_PREFIX = "&7[&bFarming&7] &r";
    public static final String CHAT_FISHING_PREFIX = "&7[&bFishing&7] &r";
    public static final String CHAT_ERROR_PREFIX = CHAT_PREFIX + "&b&lError &r";

    // Console messages
    public static final String PLUGIN_ENABLED = CONSOLE_PREFIX + "Enabled";
    public static final String PLUGIN_DISABLED = CONSOLE_PREFIX + "Disabled";
    public static final String PLUGIN_DATABASE_CONNECTED = CONSOLE_PREFIX + "Successfully connected to database!";
    public static final String PLUGIN_MODULE_LOADED = CONSOLE_PREFIX + "Module '%s' was successfully loaded!";
    public static final String PLUGIN_CONFIG_LOADED = CONSOLE_PREFIX + "Successfully loaded config!";
    public static final String PLUGIN_ECONOMY_LOADED = CONSOLE_PREFIX + "Successfully loaded economy!";
    public static final String PLUGIN_CONFIG_LOAD_FAILED = CONSOLE_PREFIX + "Failed to load config!";
    public static final String PLUGIN_ECONOMY_LOAD_FAILED = CONSOLE_PREFIX + "Failed to initialize economy!";
    public static final String PLUGIN_REDPROTECT_LOAD_FAILED = CONSOLE_PREFIX + "RedProtect plugin could not be found or is disabled!";

    // Errors
    public static final String ERROR_NO_COMMAND_PERMISSION = CHAT_ERROR_PREFIX + "&7You don't have the required permission to perform that!";
    public static final String ERROR_PLAYER_NOT_IN_FARM = CHAT_ERROR_PREFIX + "&b%s &7not currently in a farm!";
    public static final String ERROR_YOU_NOT_IN_FARM = CHAT_ERROR_PREFIX + "&7You're not currently in a farm!";
    public static final String ERROR_PLAYER_ALREADY_IN_FARM = CHAT_ERROR_PREFIX + "&b%s &7not currently in a farm!";
    public static final String ERROR_YOU_ALREADY_IN_FARM = CHAT_ERROR_PREFIX + "&7You're not currently in a farm!";
    public static final String ERROR_PLAYER_NOT_FOUND = CHAT_ERROR_PREFIX + "&7Player &b'%s&b' &7not found!";
    public static final String ERROR_UNKNOWN_ITEM = CHAT_ERROR_PREFIX + "&7Unknown item: &b%s";
    public static final String ERROR_MODULE_NOT_FOUND = CHAT_ERROR_PREFIX + "&7Could not find module with name: &b%s&7!";

    // Farming
    public static final String FARMING_FARM_CREATED = CHAT_PREFIX + "&7Created new farm with name &b%s&7!";
    
    //Fishing
    public static final String FISHING_CAUGHT_ENTITY = CHAT_FISHING_PREFIX + "You caught &b%s&r with your %s&r!";
    public static final String FISHING_BITE = CHAT_FISHING_PREFIX + "Something is tugging, &breel it in &rquick before it escapes&r!";
    public static final String FISHING_BITE_FAIL = CHAT_FISHING_PREFIX + "&bOh no! It got away! &rDon't worry, you'll get better with practice";
    public static final String FISHING_ERROR_HELP_ARGS = CHAT_FISHING_PREFIX + "&7Correct usage: &b/fs help [command]";
    public static final String FISHING_ERROR_GIVE_ARGS = CHAT_FISHING_PREFIX + "&7Correct usage: &b/fs give <player> <fishing_rod> [amount]";

}
