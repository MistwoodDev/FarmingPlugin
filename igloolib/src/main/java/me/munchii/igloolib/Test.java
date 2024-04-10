package me.munchii.igloolib;

import me.munchii.igloolib.profanity.ProfanityFilter;
import me.munchii.igloolib.profanity.ProfanityResult;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;

public class Test {
    public static void main(String... args) {
        if (IglooVersion.ENV.get().isDevelopment()) {
            System.out.println("a");
            String test = "you are acunt";
            ProfanityResult result = ProfanityFilter.filter(test, true);
            System.out.println(result.getSafeText());
        }

        if (IglooVersion.ENV.get().isRelease()) {

        }
    }
}
