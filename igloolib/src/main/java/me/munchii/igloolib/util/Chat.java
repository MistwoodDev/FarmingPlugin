package me.munchii.igloolib.util;

import org.bukkit.ChatColor;

public class Chat {
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
