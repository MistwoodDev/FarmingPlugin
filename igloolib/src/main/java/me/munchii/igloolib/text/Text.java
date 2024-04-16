package me.munchii.igloolib.text;

import me.munchii.igloolib.IgloolibConfig;
import me.munchii.igloolib.util.Chat;
import me.munchii.igloolib.util.KeyUtil;
import me.munchii.igloolib.util.Logger;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface Text {
    class Literal implements Text {
        private final String value;

        public Literal(String value) {
            this.value = value;
        }

        @Override
        public void send(@NotNull Player player) {
            player.sendMessage(value);
        }

        @Override
        public boolean isEmpty() {
            return value.isEmpty();
        }

        @Override
        public String toString() {
            return value;
        }
    }

    void send(@NotNull Player player);

    boolean isEmpty();

    @NotNull
    static Literal translatable(@Nullable Player player, @NotNull NamespacedKey key) {
        return translatable(player, KeyUtil.toDottedString(key));
    }

    @NotNull
    static Literal translatable(@Nullable Player player, @NotNull String key) {
        String locale;
        if (player == null) {
            locale = IgloolibConfig.defaultLocale;
        } else {
            locale = player.getLocale();
        }

        String res = LocaleManager.get(locale, key);
        if (res == null) {
            if (!player.getLocale().toLowerCase(Locale.ROOT).equals(IgloolibConfig.defaultLocale)) {
                res = LocaleManager.get(IgloolibConfig.defaultLocale, key);
                if (res != null) {
                    return new Literal(res);
                }
            }

            Logger.severe("Text: Could not find key '" + key + "' for locale '" + player.getLocale() + "'");
            return new Literal(key);
        }

        return new Literal(res);
    }

    @NotNull
    static Literal translatable(@Nullable Player player, String key, Object... args) {
        return new Literal(String.format(translatable(player, key).toString(), args));
    }

    @NotNull
    static Literal translatableColor(@Nullable Player player, String key) {
        return new Literal(Chat.color(translatable(player, key).toString()));
    }

    @NotNull
    static Literal translatableColor(@Nullable Player player, String key, Object... args) {
        return new Literal(Chat.color(translatable(player, key, args).toString()));
    }

    @NotNull
    static Literal literal(@Nullable String string) {
        return new Literal(string == null ? "" : string);
    }
}
