package me.munchii.igloolib;

import me.munchii.igloolib.config.Config;

public class IgloolibConfig {
    @Config(config = "settings", category = "general", key = "DefaultLocale", comment = "The Default Locale To Use")
    public static String defaultLocale = "en_us";

    @Config(config = "settings", category = "general", key = "Verbose", comment = "Verbose Output Mode")
    public static boolean verbose = true;
}
