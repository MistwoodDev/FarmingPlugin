package net.mistwood.FarmingPlugin.Modules.Farm;

import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Farm.Commands.*;

import static java.util.Arrays.asList;

public class FarmModule implements Module
{

    private Main Instance;

    @Override
    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        new FarmEvents (Instance);

        CommandHandler Handler =  new CommandHandler (Instance, "farm", asList ("farms", "f"));
        Handler.RegisterCommand (asList ("help", "?"), new HelpCommand (Instance));
        Handler.RegisterCommand (asList ("info", "i"), new InfoCommand (Instance));
        Handler.RegisterCommand (asList ("add", "addmember"), new AddMemberCommand (Instance));
        Handler.RegisterCommand (asList ("kick", "kickmember"), new KickMemberCommand (Instance));
        Handler.RegisterCommand (asList ("promote", "p"), new PromoteMemberCommand (Instance));
        Handler.RegisterCommand (asList ("demote", "d"), new DemoteMemberCommand (Instance));
        Handler.RegisterCommand (asList ("leave", "l"), new LeaveCommand (Instance));
        Handler.RegisterCommand (asList ("accept", "yes"), null); // TODO: Might not have to be registered like this?
        Handler.RegisterCommand (asList ("deny", "no"), null); // TODO: Might not have to be registered like this?
        Handler.RegisterCommand (asList ("rename", "r"), new RenameCommand (Instance));
    }

    @Override
    public void OnDisable () { }

    @Override
    public String GetName ()
    {
        return "FarmModule";
    }

}
