net.mistwood.FarmingPlugin.Modules.Fishing;

import org.bukkit.plugin.java.JavaPlugin;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Modules.Fishing.Commands.FishingCommand;

public class FishingModule implements Module {
	
	private Main instance;
	
	@Override
	public void onEnable(Main Instance) {
		this.instance = Instance;
		
		new FishingEvents(Instance);
	}
	
	@Override
	public void onDisable() { }

    @Override
    public String GetName() {
        return "FishingModule";
    }
}
