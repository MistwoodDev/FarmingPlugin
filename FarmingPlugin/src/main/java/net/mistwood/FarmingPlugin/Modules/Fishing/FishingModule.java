package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Modules.Fishing.Commands.*;

import static java.util.Arrays.asList;

public class FishingModule extends Module {

    public FishingModule() {
        super("FishingModule", "1.0", "fishing");
    }

    @Override
    public void onEnable() {
        new FishingEvents();

        CommandHandler commandHandler = new CommandHandler("fishing", asList("fs"));
        commandHandler.registerCommand(asList("give", "g"), new GiveCommand());
        commandHandler.registerCommand(asList("help", "?"), new HelpCommand());
    }

    @Override
    public void onDisable() { }

}
