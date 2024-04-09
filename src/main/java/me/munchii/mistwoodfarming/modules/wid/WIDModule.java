package me.munchii.mistwoodfarming.modules.wid;

import me.munchii.igloolib.block.BlockEntityManager;
import me.munchii.igloolib.block.IglooBlockEntity;
import me.munchii.igloolib.block.IglooBlockEntityType;
import me.munchii.igloolib.module.PluginModule;
import me.munchii.igloolib.text.LocaleManager;
import me.munchii.igloolib.text.Text;
import me.munchii.igloolib.util.*;
import me.munchii.mistwoodfarming.config.MistwoodFarmingConfig;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

// Was Is Das (Jade clone)
public class WIDModule extends PluginModule {
    private static BukkitTask tickTask;
    private static final Map<UUID, BossBar> BOSS_BARS = new HashMap<>();

    public WIDModule() {
        super("wid", MistwoodFarmingConfig.widModuleEnabled);
    }

    @Override
    public void onEnable() {
        tickTask = TaskManager.registerRepeatingTask("WID_BOSSBAR_TICK", () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Block targetBlock = player.getTargetBlock(null, 5);
                String blockName;
                if (targetBlock.getType() != Material.AIR && BlockEntityManager.isBlockEntityAt(targetBlock.getLocation())) {
                    IglooBlockEntity blockEntity = BlockEntityManager.getBlockEntity(targetBlock.getLocation());
                    blockName = Chat.stripColorCodes(Text.translatable(player, KeyUtil.join("block", IglooBlockEntityType.getId(blockEntity.getType()))));
                } else {
                    blockName = StringUtil.toTitleCase(targetBlock.getType().name().toLowerCase(Locale.ROOT).replace("_", " "));
                }

                if (BOSS_BARS.containsKey(player.getUniqueId())) {
                    if (targetBlock.getType() == Material.AIR) {
                        BOSS_BARS.get(player.getUniqueId()).setVisible(false);
                        continue;
                    } else {
                        BOSS_BARS.get(player.getUniqueId()).setVisible(true);
                    }

                    BOSS_BARS.get(player.getUniqueId()).setTitle(blockName);
                } else {
                    if (targetBlock.getType() == Material.AIR) {
                        continue;
                    }

                    BossBar bossBar = Bukkit.createBossBar(blockName, BarColor.WHITE, BarStyle.SOLID);
                    bossBar.addPlayer(player);
                    BOSS_BARS.put(player.getUniqueId(), bossBar);
                }
            }
        }, 1, 1, TimeUnit.TICK);
    }

    @Override
    public void onDisable() {
        TaskManager.removeTask("WID_BOSSBAR_TICK");
    }
}
