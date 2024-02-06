package me.munchii.igloolib.text;

import me.munchii.igloolib.util.Chat;
import me.munchii.igloolib.util.Logger;
import org.bukkit.entity.Player;

import java.util.Locale;

public interface Text {
    static String translatable(Player player, String key) {
        String res = LocaleManager.get(player.getLocale(), key);
        if (res == null) {
            if (!player.getLocale().toLowerCase(Locale.ROOT).equals("en_us")) {
                res = LocaleManager.get("en_us", key);
                if (res != null) {
                    return res;
                }
            }

            Logger.severe("could not find key '" + key + "' for locale '" + player.getLocale() + "'");
            return key;
        }

        return res;
    }

    static String translatableColor(Player player, String key) {
        String res = translatable(player, key);
        if (res == null) {
            return key;
        }

        return Chat.color(res);
    }
}
