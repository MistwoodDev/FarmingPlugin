package net.mistwood.FarmingPlugin.Modules.Fishing;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Modules.Fishing.Commands.*;

import static java.util.Arrays.asList;

public class FishingModule extends Module
{
    
    private Main Instance;

    public FishingModule ()
    {
        super ("FishingModule", "1.0", "fishing");
    }

    @Override
    public void OnEnable (Main Instance)
    {
        this.Instance = Instance;
        
        new FishingEvents (Instance);
        
        CommandHandler Handler = new CommandHandler(Instance, "fishing", asList("fs"));
        Handler.RegisterCommand(asList("give"), new GiveCommand(Instance));
        Handler.RegisterCommand(asList("help", "?"), new HelpCommand(Instance));
    }
    
    @Override
    public void OnDisable () { }

}
