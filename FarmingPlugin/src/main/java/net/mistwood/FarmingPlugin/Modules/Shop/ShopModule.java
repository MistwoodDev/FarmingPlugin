package net.mistwood.FarmingPlugin.Modules.Shop;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Shop.Commands.ShopCommand;

public class ShopModule extends Module
{

    private static Main Instance;

    public ShopModule ()
    {
        super ("ShopModule", "1.0", "shop");
    }

    @Override
    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        new ShopEvents (Instance);
        Instance.RegisterCommand ("shop", new ShopCommand (Instance));
    }

    @Override
    public void OnDisable () { }

}
