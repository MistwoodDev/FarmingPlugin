package net.mistwood.FarmingPlugin.Utils.Lores;

import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class LoreParser {

    public static void parse(ItemMeta meta, LoreOption... options) {
        parse(meta, ":", options);
    }

    public static void parse(ItemMeta meta, String seperator, LoreOption... options) {
        parse(Objects.requireNonNull(meta.getLore()), seperator, options);
    }

    public static void parse(List<String> lore, LoreOption... options) {
        parse(lore, ":", options);
    }

    public static void parse(List<String> lore, String seperator, LoreOption... options) {
        for (String line : lore) {
            if (line.trim().startsWith("-") && line.contains(seperator)) {
                line = line.substring(1).trim();
                String[] items = line.split(seperator);

                if (items.length == 2) {
                    String key = items[0].toLowerCase().trim();
                    String value = items[1].trim();

                    for (LoreOption option : options) {
                        if (option.key.toLowerCase().equals(key)) {
                            option.setValue(value);
                        }
                    }
                }
            }
        }
    }

}
