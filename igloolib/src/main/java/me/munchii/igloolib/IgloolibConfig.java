package me.munchii.igloolib;

import me.munchii.igloolib.config.Config;

public class IgloolibConfig {
    @Config(config = "modules", category = "general", key = "DefaultLocale", comment = "The Default Locale To Use")
    public static String defaultLocale = "en_us";
}
