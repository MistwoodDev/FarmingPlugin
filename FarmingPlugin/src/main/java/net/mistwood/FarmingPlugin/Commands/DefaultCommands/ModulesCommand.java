package net.mistwood.FarmingPlugin.Commands.DefaultCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;
import net.mistwood.FarmingPlugin.FarmingPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModulesCommand implements SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            Module module = FarmingPlugin.instance.getModuleByName(args[0]);

            if (module == null) {
                Helper.sendMessage(sender, String.format(Messages.ERROR_MODULE_NOT_FOUND, args[0]));
                return true;
            }

            String header = "&7--- Module: &b" + module.getName() + " &7---";
            Helper.sendMessage(sender, header);
            Helper.sendMessage(sender, "&7Help: &b/" + module.getMainCommand() + "&b ?");
            Helper.sendMessage(sender, "&7" + String.join("", Collections.nCopies(header.replace("&7", "").replace("&b", "").length(), "-")));

            return true;
        } else {
            String header = "&7--- Modules ---";
            Helper.sendMessage(sender, header);

            for (Module module : FarmingPlugin.instance.modules) {
                if (module.getMainCommand().isEmpty())
                    Helper.sendMessage(sender, "&7- " + module.getName());
                else
                    Helper.sendMessage(sender, "&7- " + module.getName() + "&7 : &b/" + module.getMainCommand() + "&b ?");
            }

            Helper.sendMessage(sender, "&7" + String.join("", Collections.nCopies(header.replace("&7", "").replace("&b", "").length(), "-")));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1)
            if (args[0].isEmpty())
                return FarmingPlugin.instance.modules.stream().map(Module::getName).collect(Collectors.toList());
            else
                return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(args[0].toLowerCase())).map(Player::getName).collect(Collectors.toList());

        return new ArrayList<>();
    }

}
