package net.mistwood.FarmingPlugin.Modules.Fishing.Commands;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;

public class CommandHelper {

    public static void HandleHelp(Main Instance, Player Sender, String Command) {

    }

    public static void HandleGive(Main Instance, Player Target, String[] Args) {
    	if (Args.length > 3) {
            
            Target.sendMessage(Messages.GiveArgsError);
    	}
    }

}
