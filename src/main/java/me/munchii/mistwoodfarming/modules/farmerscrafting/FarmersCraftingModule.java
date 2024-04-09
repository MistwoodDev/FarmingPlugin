package me.munchii.mistwoodfarming.modules.farmerscrafting;

import me.munchii.igloolib.module.PluginModule;
import me.munchii.mistwoodfarming.config.MistwoodFarmingConfig;

public class FarmersCraftingModule extends PluginModule {
    public FarmersCraftingModule() {
        super("farmerscrafting", MistwoodFarmingConfig.farmersCraftingModuleEnabled);
    }

    // TODO maybe a cool crafting grid like 6x6 or 9x9 or something like that so we can create many complicated recipes
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
