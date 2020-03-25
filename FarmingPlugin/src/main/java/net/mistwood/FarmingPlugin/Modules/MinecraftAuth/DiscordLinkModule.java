package net.mistwood.FarmingPlugin.Modules.MinecraftAuth;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.MinecraftAuth.Commands.LinkCommand;

public class DiscordLinkModule implements Module
{

    private static Main Instance;

    @Override
    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        Instance.RegisterCommand ("link", new LinkCommand (Instance));
    }

    @Override
    public void OnDisable () { }

    @Override
    public String GetName ()
    {
        return "DiscordLinkModule";
    }

}
