package net.mistwood.FarmingPlugin.Commands.DefaultCommands;

import net.mistwood.FarmingPlugin.Commands.SubCommand;
import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Module;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModulesCommand implements SubCommand
{

    private Main Instance;

    public ModulesCommand (Main Instance)
    {
        this.Instance = Instance;
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        if (Args.length == 1)
        {
            Module TargetModule = Instance.GetModuleByName (Args[0]);

            if (TargetModule == null)
            {
                Helper.SendMessage (Sender, String.format (Messages.ModuleNotFound, Args[0]));
                return true;
            }

            String Header = "&7--- Module: &b" + TargetModule.GetName () + " &7---";
            Helper.SendMessage (Sender, Header);
            Helper.SendMessage (Sender, "&7Help: &b/" + TargetModule.GetMainCommand () + "&b ?");
            Helper.SendMessage (Sender, "&7" + String.join ("", Collections.nCopies (Header.replace ("&7", "").replace ("&b", "").length (), "-")));

            return true;
        }

        else
        {
            String Header = "&7--- Modules ---";
            Helper.SendMessage (Sender, Header);

            for (Module Mod : Instance.Modules)
            {
                if (Mod.GetMainCommand ().isEmpty ())
                    Helper.SendMessage (Sender, "&7- " + Mod.GetName ());
                else
                    Helper.SendMessage (Sender, "&7- " + Mod.GetName () + "&7 : &b/" + Mod.GetMainCommand () + "&b ?");
            }

            Helper.SendMessage (Sender, "&7" + String.join ("", Collections.nCopies (Header.replace ("&7", "").replace ("&b", "").length (), "-")));

            return true;
        }
    }

    @Override
    public List<String> onTabComplete (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        if (Args.length == 1)
            if (Args[0].isEmpty ())
                return Instance.Modules.stream ().map (Module::GetName).collect(Collectors.toList ());
            else
                return Bukkit.getOnlinePlayers ().stream ().filter (Player -> Player.getName ().toLowerCase ().startsWith (Args[0].toLowerCase ())).map (Player::getName).collect (Collectors.toList ());

        return new ArrayList<> ();
    }

}
