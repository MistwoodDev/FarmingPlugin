package me.munchii.igloolib.text;

import me.munchii.igloolib.util.Chat;
import me.munchii.igloolib.util.Logger;
import org.bukkit.entity.Player;

import java.util.Locale;

public interface Text {
    class Literal implements Text {
        private final String value;

        public Literal(String value) {
            this.value = value;
        }

        @Override
        public void send(Player player) {
            player.sendMessage(value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    void send(Player player);

    static Literal translatable(Player player, String key) {
        String res = LocaleManager.get(player.getLocale(), key);
        if (res == null) {
            if (!player.getLocale().toLowerCase(Locale.ROOT).equals("en_us")) {
                res = LocaleManager.get("en_us", key);
                if (res != null) {
                    return new Literal(res);
                }
            }

            Logger.severe("could not find key '" + key + "' for locale '" + player.getLocale() + "'");
            return new Literal(key);
        }

        return new Literal(res);
    }

    static Literal translatable(Player player, String key, Object... args) {
        return new Literal(String.format(translatable(player, key).toString(), args));
    }

    static Literal translatableColor(Player player, String key) {
        return new Literal(Chat.color(translatable(player, key).toString()));
    }

    static Literal translatableColor(Player player, String key, Object... args) {
        return new Literal(Chat.color(translatable(player, key, args).toString()));
    }
}
