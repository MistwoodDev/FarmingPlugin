package me.munchii.mistwoodfarming;

import me.munchii.igloolib.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    public EventListener() {
        Bukkit.getPluginManager().registerEvents(this, MistwoodFarming.INSTANCE);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Chat.sendTranslatable(event.getPlayer(), "message.mistwoodfarming.player_welcome");
    }
}
