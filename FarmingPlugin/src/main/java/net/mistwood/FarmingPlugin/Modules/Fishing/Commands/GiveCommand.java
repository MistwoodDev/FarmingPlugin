package net.mistwood.FarmingPlugin.Modules.Fishing.Commands;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GiveCommand implements SubCommand {
	
	private Main Instance;
	
	public GiveCommand (Main Instance) {
		this.Instance = Instance;
	}
	
	@Override
	public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
		if (Args.length > 0 && (Sender instanceof Player && Instance.PermissionManager.HasCommandPermission(Sender, "give")))
        {
            Player Target = (Player) Sender;

            CommandHelper.HandleGive(Instance, Target, Args);

            return true;
        }
		else return false;
	}
	
    @Override
    public List<String> onTabComplete (CommandSender Sender, Command Command, String Alias, String[] Args)
    {
        if (Args.length == 1)
            if (Args[0].isEmpty())
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            else
                return Bukkit.getOnlinePlayers().stream().filter(Player -> Player.getName().toLowerCase().startsWith(Args[0].toLowerCase())).map(Player::getName).collect(Collectors.toList());

        return new ArrayList<> ();
    }
}