package net.mistwood.FarmingPlugin.Utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Helper
{

    public static void SendMessage (CommandSender Target, String Message)
    {
        Target.sendMessage (Colorfy (Message));
    }

    public static void SendMessage (Player Target, String Message)
    {
        Target.sendMessage (Colorfy (Message));
    }

    public static void DisplayActionBar (Player Target, String Message)
    {
        Target.spigot ().sendMessage (ChatMessageType.ACTION_BAR, new TextComponent (Colorfy (Message)));
    }

    public static String Colorfy (String Message)
    {
        return ChatColor.translateAlternateColorCodes ('&', Message);
    }

    public static ItemStack ItemFromString (String Item)
    {
        return new ItemStack (Material.getMaterial (Item.trim ()), 1);
    }

    public static List<String> UUIDListToString (List<UUID> IDs)
    {
        List<String> StringIDs = new ArrayList<String> ();
        for (UUID ID : IDs)
        {
            StringIDs.add (ID.toString ());
        }

        return StringIDs;
    }

    public static List<UUID> StringUUIDToUUIDList (List<String> StringIDs)
    {
        List<UUID> IDs = new ArrayList<UUID> ();
        for (String ID : StringIDs)
        {
            IDs.add (UUID.fromString (ID));
        }

        return IDs;
    }

}
