package net.mistwood.FarmingPlugin.Modules.Farm;

import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Farm.Commands.*;

import static java.util.Arrays.asList;

public class FarmModule extends Module {

    public FarmModule() {
        super("FarmModule", "1.1", "farms");
    }

    @Override
    public void onEnable() {
        new FarmEvents();

        CommandHandler commandHandler = new CommandHandler("farm", asList("farms", "f"));
        commandHandler.registerCommand(asList("help", "?"), new HelpCommand());
        commandHandler.registerCommand(asList("info", "i"), new InfoCommand());
        commandHandler.registerCommand(asList("add", "addmember"), new AddMemberCommand());
        commandHandler.registerCommand(asList("kick", "kickmember"), new KickMemberCommand());
        commandHandler.registerCommand(asList("promote", "p"), new PromoteMemberCommand());
        commandHandler.registerCommand(asList("demote", "d"), new DemoteMemberCommand());
        commandHandler.registerCommand(asList("leave", "l"), new LeaveCommand());
        commandHandler.registerCommand(asList("accept", "yes"), null); // TODO: Might not have to be registered like this?
        commandHandler.registerCommand(asList("deny", "no"), null); // TODO: Might not have to be registered like this?
        commandHandler.registerCommand(asList("rename", "r"), new RenameCommand());
    }

    @Override
    public void onDisable() { }

}
