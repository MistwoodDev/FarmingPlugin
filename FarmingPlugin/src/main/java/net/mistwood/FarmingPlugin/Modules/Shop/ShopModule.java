package net.mistwood.FarmingPlugin.Modules.Shop;

import net.mistwood.FarmingPlugin.FarmingPlugin;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Shop.Commands.ShopCommand;

public class ShopModule extends Module {

    public ShopModule() {
        super("ShopModule", "1.0", "shop");
    }

    @Override
    public void onEnable() {
        new ShopEvents();

        FarmingPlugin.instance.registerCommand("shop", new ShopCommand());
    }

    @Override
    public void onDisable() { }

}
