package net.mistwood.FarmingPlugin.Modules.Shop;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Shop.Commands.ShopCommand;

public class ShopModule implements Module
{

    private static Main Instance;

    @Override
    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        new ShopEvents (Instance);
        Instance.RegisterCommand ("shop", new ShopCommand (Instance));
    }

    @Override
    public void OnDisable () { }

    @Override
    public String GetName ()
    {
        return "ShopModule";
    }

}
