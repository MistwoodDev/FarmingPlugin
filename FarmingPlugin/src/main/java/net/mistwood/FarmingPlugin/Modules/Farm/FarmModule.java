package net.mistwood.FarmingPlugin.Modules.Farm;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;

public class FarmModule implements Module
{

    private static Main Instance;

    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        new FarmEvents (Instance);
    }

    public void OnDisable ()
    {

    }

}
