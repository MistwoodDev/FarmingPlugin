package me.munchii.mistwoodfarming;

import me.munchii.igloolib.util.Chat;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Chat.sendTranslatable(event.getPlayer(), "message.mistwoodfarming.player_welcome");
    }
}
