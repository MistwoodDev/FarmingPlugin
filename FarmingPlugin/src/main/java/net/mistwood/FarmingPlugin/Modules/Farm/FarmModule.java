package net.mistwood.FarmingPlugin.Modules.Farm;

import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Farm.Commands.AddMemberCommand;

public class FarmModule implements Module
{

    private static Main Instance;

    @Override
    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;

        new FarmEvents (Instance);

        CommandHandler Handler =  new CommandHandler (Instance, "farm");
        Handler.RegisterCommand ("addmember", new AddMemberCommand (Instance));
    }

    @Override
    public void OnDisable () { }

    @Override
    public String GetName ()
    {
        return "FarmModule";
    }

}
