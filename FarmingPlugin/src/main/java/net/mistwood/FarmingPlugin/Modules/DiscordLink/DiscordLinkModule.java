package net.mistwood.FarmingPlugin.Modules.DiscordLink;

import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.DiscordLink.Commands.LinkCommand;

import static java.util.Arrays.asList;

public class DiscordLinkModule extends Module
{

    private static Main Instance;

    public DiscordLinkModule ()
    {
        super ("DiscordLinkModule", "1.0", "discord");
    }

    @Override
    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        CommandHandler Handler = new CommandHandler (Instance, "discord", asList ("dlink"));
        Handler.RegisterCommand (asList ("link", "l"), new LinkCommand (Instance));
    }

    @Override
    public void OnDisable () { }

}
