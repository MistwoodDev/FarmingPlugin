package me.munchii.igloolib.util;

import me.munchii.igloolib.text.Text;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
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

    public static void sendActionBar(Player player, String message) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        //IChatBaseComponent.ChatSerializer.fromJson(json)
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        ClientboundSystemChatPacket packet = new ClientboundSystemChatPacket(cbc, true);


        // craftplayer.getHandle().connection.send(packet)
        craftPlayer.getHandle().c.a(packet);
    }
}
