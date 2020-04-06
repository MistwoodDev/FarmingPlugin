package ca.fried_fetus69.fishingplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static Main instance;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		getLogger().info("FishingPlugin enabled");
		//getCommand("shop").setExecutor(new CommandShop());
		//getServer().getPluginManager().registerEvents(new MyPluginListener(this), this);
	}
	
	public void enterCommand(String command) {
		Main.instance.getServer().dispatchCommand(Main.instance.getServer().getConsoleSender(), command);
	}
	
	@Override
	public void onDisable() {
		System.out.println("FishingPlugin disabled");
	}
	
}