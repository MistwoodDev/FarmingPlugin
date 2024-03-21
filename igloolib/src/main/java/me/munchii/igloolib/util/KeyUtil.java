package me.munchii.igloolib.util;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class KeyUtil {
    @NotNull
    public static NamespacedKey join(String namespace, @NotNull NamespacedKey key) {
        return new NamespacedKey(namespace, toDottedString(key));
    }

    @NotNull
    public static NamespacedKey join(@NotNull NamespacedKey key, String value) {
        // TODO does this need to change?
        return new NamespacedKey(key.toString(), value);
    }

    @NotNull
    public static String toDottedString(@NotNull NamespacedKey key) {
        return key.toString().replace(":", ".");
    }
}
