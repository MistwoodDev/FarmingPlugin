package me.munchii.igloolib.text;

import org.bukkit.entity.Player;

public interface Text {
    // TODO: should we get the plugin for each?
    // we need to store read lang files so we dont have to keep reading them
    // maybe make a like LocaleManager or LangManager or MessageManager
    // and have plugins register their lang files or have it auto detect them or something
    // but would we even need to have the plugin as argument then? if we get by key
    // just have each key contain the plugin id/name
    static String translatable(Player player, String key) {
        return "";
    }
}
