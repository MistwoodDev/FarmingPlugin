package net.mistwood.FarmingPlugin.Modules.DiscordLink;

import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.DiscordLink.Commands.LinkCommand;

import static java.util.Arrays.asList;

public class DiscordLinkModule extends Module {

    public DiscordLinkModule() {
        super("DiscordLinkModule", "1.0", "discord");
    }

    @Override
    public void onEnable() {

        CommandHandler commandHandler = new CommandHandler("discord", asList("dlink"));
        commandHandler.registerCommand(asList("link", "l"), new LinkCommand());
    }

    @Override
    public void onDisable() { }

}
