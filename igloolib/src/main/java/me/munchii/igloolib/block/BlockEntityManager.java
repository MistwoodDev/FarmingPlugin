package me.munchii.igloolib.block;

import me.munchii.igloolib.util.TaskManager;
import me.munchii.igloolib.util.TimeUnit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum BlockEntityManager {
    INSTANCE;

    private final Map<Location, IglooBlockEntity> existingBlockEntities;

    BlockEntityManager() {
        existingBlockEntities = new HashMap<>();
        createTickTask();
    }

    static void clearBlockEntities() {
        INSTANCE.existingBlockEntities.clear();
    }

    @NotNull
    public static Block addBlockEntity(@NotNull Location pos, @NotNull IglooBlockEntity blockEntity) {
        INSTANCE.existingBlockEntities.put(pos, blockEntity);
        return Objects.requireNonNull(pos.getWorld()).getBlockAt(pos);
    }

    public static IglooBlockEntity removeBlockEntity(@NotNull Location pos) {
        return INSTANCE.existingBlockEntities.remove(pos);
    }

    @Nullable
    public static IglooBlockEntity getBlockEntity(@NotNull Location pos) {
        return INSTANCE.existingBlockEntities.getOrDefault(pos, null);
    }

    public static boolean isBlockEntityAt(@NotNull Location pos) {
        return INSTANCE.existingBlockEntities.containsKey(pos);
    }

    private static void createTickTask() {
        // TODO: some warning/error is thrown when creating multiple and using async task (test)
        TaskManager.registerRepeatingTask("igloolib__BlockEntityManager_tick", () -> {
            INSTANCE.existingBlockEntities.forEach((pos, entity) -> {
                if (pos.getChunk().isLoaded()) {
                    entity.tick(pos.getWorld(), pos, Objects.requireNonNull(pos.getWorld()).getBlockAt(pos));
                }
            });
        }, 1, 1, TimeUnit.TICK);
    }
}
