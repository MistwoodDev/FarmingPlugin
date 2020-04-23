package net.mistwood.FarmingPlugin.Modules.Fishing;

import org.bukkit.plugin.java.JavaPlugin;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Commands.CommandHandler;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Fishing.Commands.*;

import static java.util.Arrays.asList;

public class FishingModule implements Module {
    
    private Main instance;
    
    @Override
    public void OnEnable(Main Instance) {
        this.instance = Instance;
        
        new FishingEvents(Instance);
        
        CommandHandler Handler = new CommandHandler(Instance, "fishing", asList("fs"));
        Handler.RegisterCommand(asList("give"), new GiveCommand(Instance));
    }
    
    @Override
    public void OnDisable() { }

    @Override
    public String GetName() {
        return "FishingModule";
    }
}
