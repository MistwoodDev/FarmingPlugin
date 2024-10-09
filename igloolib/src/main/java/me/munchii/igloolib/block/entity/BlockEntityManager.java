package me.munchii.igloolib.block.entity;

import com.google.common.collect.ImmutableSet;
import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.IgloolibConfig;
import me.munchii.igloolib.nms.NbtCompound;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.util.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R2.persistence.CraftPersistentDataContainer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public enum BlockEntityManager {
    INSTANCE;

    private static final NamespacedKey BLOCK_ENTITIES_KEY = new NamespacedKey(Igloolib.INSTANCE, "IglooBlockEntity");

    private final Map<Location, IglooBlockEntity> existingBlockEntities;

    BlockEntityManager() {
        existingBlockEntities = new HashMap<>();
        createTickTask();
    }

    public static void clearBlockEntities() {
        INSTANCE.existingBlockEntities.clear();
    }

    @ApiStatus.Internal
    public static void clearChunkData(@NotNull Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY)) {
            chunk.getPersistentDataContainer().remove(BLOCK_ENTITIES_KEY);
        }
    }

    @NotNull
    public static Block addBlockEntity(@NotNull Location pos, @NotNull IglooBlockEntity blockEntity) {
        INSTANCE.existingBlockEntities.put(pos, blockEntity);

        Logger.severe("Added block entity " + IglooBlockEntityType.getId(blockEntity.getType()).toString() + " at " + pos);
        writeBlockEntityToChunk(pos, blockEntity);

        return Objects.requireNonNull(pos.getWorld()).getBlockAt(pos);
    }

    public static IglooBlockEntity removeBlockEntity(@NotNull Location pos) {
        removeBlockEntityFromChunk(pos);
        return INSTANCE.existingBlockEntities.remove(pos);
    }

    @Nullable
    public static IglooBlockEntity getBlockEntity(@NotNull Location pos) {
        IglooBlockEntity blockEntity = INSTANCE.existingBlockEntities.getOrDefault(pos, null);
        if (blockEntity == null) return null;
        return blockEntity;
    }

    @Nullable
    public static <E extends IglooBlockEntity> E getBlockEntityAt(@NotNull Location pos) {
        return (E) INSTANCE.existingBlockEntities.getOrDefault(pos, null);
    }

    public static boolean isBlockEntityAt(@NotNull Location pos) {
        return INSTANCE.existingBlockEntities.containsKey(pos);
    }

    @ApiStatus.Internal
    public static void writeBlockEntityToChunk(@NotNull Location pos, @NotNull IglooBlockEntity blockEntity) {
        NamespacedKey registryKey = IglooRegistry.BLOCK_ENTITY_TYPE.getId(blockEntity.getType());
        if (registryKey == null) {
            // TODO: error?
            return;
        }

        Chunk chunk = pos.getChunk();

        CraftPersistentDataContainer pdc = blockEntity.createNbtWithIdentifyingData().toPersistentDataContainer();

        PersistentDataContainer[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY);
        if (blockEntities == null) {
            blockEntities = new PersistentDataContainer[0];
        }

        List<PersistentDataContainer> blockEntitiesList = new ArrayList<>(Arrays.stream(blockEntities).toList());
        blockEntitiesList.add(pdc);

        chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY, blockEntitiesList.toArray(new PersistentDataContainer[0]));
    }

    @ApiStatus.Internal
    public static void saveBlockEntitiesFromChunk(@NotNull Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY)) {
            PersistentDataContainer[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY);
            if (blockEntities == null) return;

            List<PersistentDataContainer> newBlockEntities = new ArrayList<>();
            for (PersistentDataContainer pdc : blockEntities) {
                CraftPersistentDataContainer cpdc = (CraftPersistentDataContainer) pdc;
                NbtCompound nbt = new NbtCompound(cpdc.toTagCompound());
                Location blockPos = chunk.getWorld().getBlockAt(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")).getLocation();

                if (INSTANCE.existingBlockEntities.containsKey(blockPos)) {
                    IglooBlockEntity blockEntity = INSTANCE.existingBlockEntities.get(blockPos);

                    if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully saved block entity '" + nbt.getString("id") + "' at " + blockPos);
                    newBlockEntities.add(blockEntity.createNbtWithIdentifyingData().toPersistentDataContainer());
                } else {
                    // TODO: how?
                    // TODO: is this even possible (maybe very edge-case), because when chunk is loaded it's put into existingBlockEntities, so should be at unload as well right?
                    Logger.severe("BlockEntityManager: Unable to save block entity '" + nbt.getString("id") + "' at " + blockPos);
                }
            }

            chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY, newBlockEntities.toArray(new PersistentDataContainer[0]));
        }
    }

    @ApiStatus.Internal
    public static void removeBlockEntityFromChunk(@NotNull Location pos) {
        Chunk chunk = pos.getChunk();
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY)) {
            PersistentDataContainer[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY);
            if (blockEntities == null) return;

            List<PersistentDataContainer> newBlockEntities = new ArrayList<>();
            for (PersistentDataContainer pdc : blockEntities) {
                CraftPersistentDataContainer cpdc = (CraftPersistentDataContainer) pdc;
                NbtCompound nbt = new NbtCompound(cpdc.toTagCompound());

                if (pos.getBlockX() == nbt.getInt("x") && pos.getBlockY() == nbt.getInt("y") && pos.getBlockZ() == nbt.getInt("z")) {
                    if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully removed block entity '" + nbt.getString("id") + "' at " + pos);
                    continue;
                }

                newBlockEntities.add(pdc);
            }

            chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY, newBlockEntities.toArray(new PersistentDataContainer[0]));
        }
    }

    @ApiStatus.Internal
    public static void loadBlockEntitiesFromChunk(@NotNull Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY)) {
            PersistentDataContainer[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY);
            if (blockEntities == null) return;

            for (PersistentDataContainer pdc : blockEntities) {
                CraftPersistentDataContainer cpdc = (CraftPersistentDataContainer) pdc;
                NbtCompound nbt = new NbtCompound(cpdc.toTagCompound());

                Location blockPos = chunk.getWorld().getBlockAt(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")).getLocation();
                IglooBlockEntity blockEntity = IglooBlockEntity.createFromNbt(blockPos, nbt);
                if (blockEntity == null) {
                    Logger.severe("BlockEntityManager: Failed to load block entity of id '" + nbt.getString("id") + "' because of previous errors!");
                } else {
                    INSTANCE.existingBlockEntities.put(blockPos, blockEntity);
                    if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully loaded block entity '" + nbt.getString("id") + "' at " + blockPos);
                }
            }
        }
    }

    @ApiStatus.Internal
    public static Set<IglooBlockEntity> getBlockEntities() {
        return ImmutableSet.copyOf(INSTANCE.existingBlockEntities.values());
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

    public static class ChunkListener implements Listener {
        @EventHandler
        public void onChunkLoaded(ChunkLoadEvent event) {
            loadBlockEntitiesFromChunk(event.getChunk());
        }

        @EventHandler
        public void onChunkUnloaded(ChunkUnloadEvent event) {
            saveBlockEntitiesFromChunk(event.getChunk());
        }

        @EventHandler
        public void onWorldSave(WorldSaveEvent event) {
            Arrays.stream(event.getWorld().getLoadedChunks()).forEach(BlockEntityManager::saveBlockEntitiesFromChunk);
        }
    }
}
