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

    public static String stripColorCodes(Text text) {
        return stripColorCodes(text.toString());
    }

    public static String stripColorCodes(String s) {
        StringBuilder newString = new StringBuilder();
        boolean inCode = false;
        for (char c : s.toCharArray()) {
            if (c == '&') {
                inCode = true;
                continue;
            }

            if (inCode) {
                inCode = false;
                continue;
            }

            newString.append(c);
        }

        return newString.toString();
    }
}
