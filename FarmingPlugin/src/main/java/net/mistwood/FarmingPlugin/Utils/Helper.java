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
import java.util.Objects;
import java.util.UUID;

public class Helper {

    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(colorfy(msg));
    }

    public static void sendMessage(Player player, String msg) {
        player.sendMessage(colorfy(msg));
    }

    public static void displayActionBar(Player player, String msg) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colorfy(msg)));
    }

    public static String colorfy(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static ItemStack itemFromString(String item) {
        return new ItemStack(Objects.requireNonNull(Material.getMaterial(item.trim())), 1);
    }

    public static List<String> uuidsToStrings(List<UUID> uuids) {
        List<String> strings = new ArrayList<>();
        for (UUID uuid : uuids)
            strings.add(uuid.toString());

        return strings;
    }

    public static List<UUID> stringsToUUIDs(List<String> strings) {
        List<UUID> uuids = new ArrayList<UUID>();
        for (String uuid : strings)
            uuids.add(UUID.fromString(uuid));

        return uuids;
    }

    public static String capitalize(String s) {
        StringBuilder builder = new StringBuilder();

        for (String word : s.toLowerCase().split("\\s")) {
            String first = word.substring(0, 1).toUpperCase();
            String rest = word.substring(1);
            builder.append(first).append(rest).append(" ");
        }

        return builder.toString().trim();
    }

}
