package me.munchii.mistwoodfarming.config;

import me.munchii.igloolib.config.Config;

public class MistwoodFarmingConfig {
    @Config(config = "modules", category = "farming", key = "Enabled", comment = "Should the farming module be enabled")
    public static boolean farmingModuleEnabled = true;

    @Config(config = "modules", category = "shop", key = "Enabled", comment = "Should the shop module be enabled")
    public static boolean shopModuleEnabled = true;
}
