package me.munchii.mistwoodfarming.modules.wid;

import me.munchii.igloolib.block.BlockEntityManager;
import me.munchii.igloolib.block.IglooBlockEntity;
import me.munchii.igloolib.block.IglooBlockEntityType;
import me.munchii.igloolib.module.PluginModule;
import me.munchii.igloolib.text.Text;
import me.munchii.igloolib.util.*;
import me.munchii.mistwoodfarming.config.MistwoodFarmingConfig;
import me.munchii.mistwoodfarming.modules.wid.api.WIDInformable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

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

                    if (blockEntity instanceof WIDInformable informable) {
                        blockName += "\n§n" + informable.getInformation();
                    }
                } else {
                    blockName = StringUtil.toTitleCase(targetBlock.getType().name().toLowerCase(Locale.ROOT).replace("_", " "));
                }

                Location lookingAt = player.getEyeLocation();
                Set<Entity> entities = player.getWorld().getNearbyEntities(lookingAt, 5, 5, 5)
                        .stream().filter(entity -> !(entity instanceof Player) && !entity.isDead()).collect(Collectors.toSet());
                if (!entities.isEmpty()) {
                    for (Entity entity : entities) {
                        if (isLookingAtEntity(player, (LivingEntity) entity)) {
                            // TODO: check for custom entities when implemented

                            blockName = entity.getName();
                        }
                    }
                }

                Chat.sendActionBar(player, blockName);

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

    private static boolean isLookingAtEntity(Player player, LivingEntity entity) {
        Location lookingAt = player.getEyeLocation();
        Vector toEntity = entity.getEyeLocation().toVector().subtract(lookingAt.toVector());
        double dot = toEntity.normalize().dot(lookingAt.getDirection());

        return dot > 0.99D;
    }
}
