package net.mistwood.FarmingPlugin.Commands;

import net.mistwood.FarmingPlugin.Main;
import net.mistwood.FarmingPlugin.Utils.Helper;
import net.mistwood.FarmingPlugin.Utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.lang.reflect.Array;
import java.util.*;

public class CommandHandler implements CommandExecutor, TabCompleter, Listener
{

    private Main Instance;
    private String Command;
    private List<String> Aliases;
    private Map<String, SubCommand> CommandMap = new HashMap<String, SubCommand> ();
    private Map<String, String> CommandConfirm = new HashMap<String, String> ();

    public CommandHandler (Main Instance, String Command)
    {
        this (Instance, Command, Arrays.asList ());
    }

    public CommandHandler (Main Instance, String Command, List<String> Aliases)
    {
        this.Instance = Instance;
        this.Command = Command;
        this.Aliases = Aliases;

        Instance.getCommand (Command).setExecutor (this);
        Instance.getCommand (Command).setTabCompleter (this);
    }

    @Override
    public boolean onCommand (CommandSender Sender, Command Command, String Label, String[] Args)
    {
        if (Args.length > 0 && HasCommand (Args[0]))
        {
            CommandExecutor Executor = GetCommandSubCommand (Args[0]);
            return Executor.onCommand (Sender, Command, Label, CopyArrayOfRange (Args, Args.length));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete (CommandSender Sender, Command Command, String Alias, String[] Args)
    {
        if (Sender instanceof Player)
        {
            if (Args.length > 0 && HasCommand (Args[0]))
            {
                TabCompleter Completer = this.GetCommandSubCommand (Args[0]);
                return Completer.onTabComplete (Sender, Command, Alias, CopyArrayOfRange (Args, Args.length));
            }

            else
            {
                SortedSet<String> Tab = new TreeSet<String> ();

                for (String Commands : CommandMap.keySet ())
                {
                    // TODO: Check for permission
                    if (Commands.startsWith (Args[0]) && !Tab.contains (Commands))
                        Tab.add (Commands);
                }

                return new ArrayList<> (Tab);
            }
        }

        else
        {
            // TODO: Console commands?
            return new ArrayList<> ();
        }
    }

    @EventHandler
    public void onPreCommand (PlayerCommandPreprocessEvent Event)
    {
        Player Target = Event.getPlayer ();
        String[] Args = Event.getMessage ().split (" ");

        StringBuilder CommandArgsBuilder = new StringBuilder ();
        Arrays.stream (Args).forEach (Argument -> CommandArgsBuilder.append (Argument).append (" "));
        String CommandArguments = CommandArgsBuilder.substring (1, CommandArgsBuilder.length () - 1);

        if (Args.length >= 2 && Args[0].equals (Command))
        {
            // TODO: Check perms
            if (HasCommand (Args[1]))
            {
                Helper.SendMessage (Target, Messages.NoCommandPermission);
                Event.setCancelled (true);

                return;
            }
        }
    }

    public void RegisterCommand (String Command, SubCommand CommandExecutor)
    {
        CommandMap.put (Command, CommandExecutor);
    }

    private SubCommand GetCommandSubCommand (String Command)
    {
        return CommandMap.entrySet ().stream ().filter (Value -> Value.getKey ().contains (Command)).findFirst ().get ().getValue ();
    }

    private boolean HasCommand (String Command)
    {
        return CommandMap.keySet ().stream ().anyMatch (Value -> Value.contains (Command));
    }

    private static <T> T[] CopyArrayOfRange (T[] Original, int End)
    {
        int Start = 1;
        if (Original.length >= Start)
        {
            if (Start <= End)
            {
                int Length = End - Start;
                int CopyLength = Math.min (Length, Original.length - Start);
                T[] Copy = (T[]) Array.newInstance (Original.getClass ().getComponentType (), Length);

                System.arraycopy (Original, Start, Copy, 0, CopyLength);

                return Copy;
            }

            throw new IllegalArgumentException ();
        }

        throw new ArrayIndexOutOfBoundsException ();
    }

}
