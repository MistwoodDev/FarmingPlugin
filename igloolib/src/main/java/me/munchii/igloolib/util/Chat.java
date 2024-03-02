package me.munchii.igloolib.util;

import me.munchii.igloolib.text.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendTranslatable(Player player, String key) {
        player.sendMessage(Text.translatableColor(player, key).toString());
    }
}
