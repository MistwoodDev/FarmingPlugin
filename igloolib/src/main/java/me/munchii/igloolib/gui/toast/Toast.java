package me.munchii.igloolib.gui.toast;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.util.Duration;
import me.munchii.igloolib.util.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.UUID;

// TODO: doesn't work
public final class Toast {
    private final NamespacedKey key;
    private final String icon;
    private final String message;
    private final ToastStyle style;

    public Toast(String icon, String message, ToastStyle style) {
        //this.key = new NamespacedKey(Igloolib.INSTANCE, UUID.randomUUID().toString());
        this.key = new NamespacedKey(Igloolib.INSTANCE, "YEEEEET1334345343454");
        this.icon = icon;
        this.message = message;
        this.style = style;
    }

    public void displayTo(Player player, Duration duration) {
        start(player, duration);
    }

    private void start(Player player, Duration duration) {
        createAdvancement();
        grantAdvancement(player);

        TaskManager.runAnonymousDelayedTask(() -> revokeAdvancement(player), duration.time(), duration.timeUnit());
    }

    private void createAdvancement() {
        Bukkit.reloadData();
        Bukkit.getUnsafe().loadAdvancement(key, "{\n" +
                "    \"criteria\": {\n" +
                "        \"trigger\": {\n" +
                "            \"trigger\": \"minecraft:impossible\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"display\": {\n" +
                "        \"icon\": {\n" +
                "            \"item\": \"minecraft:" + icon + "\"\n" +
                "        },\n" +
                "        \"title\": {\n" +
                "            \"text\": \"" + message.replace("|", "\n") + "\"\n" +
                "        },\n" +
                "        \"description\": {\n" +
                "            \"text\": \"\"\n" +
                "        },\n" +
                "        \"background\": \"minecraft:textures/gui/advancements/backgrounds/stone.png\",\n" +
                "        \"frame\": \"" + style.name().toLowerCase() + "\",\n" +
                "        \"announce_to_chat\": false,\n" +
                "        \"show_toast\": true,\n" +
                "        \"hidden\": true\n" +
                "    },\n" +
                "    \"requirements\": [\n" +
                "        [\n" +
                "            \"trigger\"\n" +
                "        ]\n" +
                "    ]\n" +
                "}");
    }

    private void grantAdvancement(Player player) {
        player.getAdvancementProgress(Bukkit.getAdvancement(key)).awardCriteria("trigger");
    }

    private void revokeAdvancement(Player player) {
        player.getAdvancementProgress(Bukkit.getAdvancement(key)).revokeCriteria("trigger");
    }
}
