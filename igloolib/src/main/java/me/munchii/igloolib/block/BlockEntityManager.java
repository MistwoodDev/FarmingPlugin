package me.munchii.igloolib.block;

import com.mojang.datafixers.util.Pair;
import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.IgloolibConfig;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.util.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public enum BlockEntityManager {
    INSTANCE;

    private static final NamespacedKey BLOCK_ENTITIES_KEY = new NamespacedKey(Igloolib.INSTANCE, "IglooBlockEntity");

    private final Map<Location, Pair<IglooBlockEntityType<?>, IglooBlockEntity>> existingBlockEntities;

    BlockEntityManager() {
        existingBlockEntities = new HashMap<>();
        createTickTask();
    }

    static void clearBlockEntities() {
        INSTANCE.existingBlockEntities.clear();
    }

    @NotNull
    public static Block addBlockEntity(@NotNull Location pos, @NotNull IglooBlockEntityType<?> blockEntityType, @NotNull IglooBlockEntity blockEntity) {
        INSTANCE.existingBlockEntities.put(pos, new Pair<>(blockEntityType, blockEntity));

        writeBlockEntityToChunk(pos, blockEntityType, blockEntity);

        return Objects.requireNonNull(pos.getWorld()).getBlockAt(pos);
    }

    public static IglooBlockEntity removeBlockEntity(@NotNull Location pos) {
        removeBlockEntityFromChunk(pos);
        return INSTANCE.existingBlockEntities.remove(pos).getSecond();
    }

    @Nullable
    public static IglooBlockEntity getBlockEntity(@NotNull Location pos) {
        Pair<IglooBlockEntityType<?>, IglooBlockEntity> res = INSTANCE.existingBlockEntities.getOrDefault(pos, null);
        if (res == null) return null;
        return res.getSecond();
    }

    public static boolean isBlockEntityAt(@NotNull Location pos) {
        return INSTANCE.existingBlockEntities.containsKey(pos);
    }

    @ApiStatus.Internal
    public static void writeBlockEntityToChunk(@NotNull Location pos, @NotNull IglooBlockEntityType<?> blockEntityType, @NotNull IglooBlockEntity blockEntity) {
        NamespacedKey registryKey = IglooRegistry.BLOCK_ENTITY_TYPE.getId(blockEntityType);

        Chunk chunk = pos.getChunk();
        String info = String.join(",",
                // xyz
                String.valueOf(pos.getBlockX()),
                String.valueOf(pos.getBlockY()),
                String.valueOf(pos.getBlockZ()),
                // block entity type
                registryKey.toString())
                // custom data
                + ";"
                + blockEntity.writeChunkData()
                .entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));

        String[] blockEntities = new String[0];
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY)) {
            blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY);
            if (blockEntities == null) {
                blockEntities = new String[0];
            }
        }

        List<String> blockEntitiesList = new ArrayList<>(Arrays.stream(blockEntities).toList());
        blockEntitiesList.add(info);

        chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY, blockEntitiesList.toArray(new String[0]));
    }

    @ApiStatus.Internal
    public static void saveBlockEntitiesFromChunk(@NotNull Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY)) {
            String[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY);
            if (blockEntities == null) return;

            List<String> newBlockEntities = new ArrayList<>();
            for (String info : blockEntities) {
                String[] parts = info.split(";");
                if (parts.length < 1) return;

                String data = parts[0];
                String[] dataParts = data.split(",");
                int blockX = Integer.parseInt(dataParts[0]);
                int blockY = Integer.parseInt(dataParts[1]);
                int blockZ = Integer.parseInt(dataParts[2]);

                Location blockPos = chunk.getWorld().getBlockAt(blockX, blockY, blockZ).getLocation();
                if (INSTANCE.existingBlockEntities.containsKey(blockPos)) {
                    Pair<IglooBlockEntityType<?>, IglooBlockEntity> blockEntityPair = INSTANCE.existingBlockEntities.get(blockPos);
                    String newInfo = String.join(",",
                            // xyz
                            String.valueOf(blockPos.getBlockX()),
                            String.valueOf(blockPos.getBlockY()),
                            String.valueOf(blockPos.getBlockZ()),
                            // block entity type
                            dataParts[3])
                            // custom data
                            + ";"
                            + blockEntityPair.getSecond().writeChunkData()
                            .entrySet().stream()
                            .map(entry -> entry.getKey() + ":" + entry.getValue())
                            .collect(Collectors.joining(","));

                    if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully saved block entity '" + dataParts[3] + "' at " + blockPos);
                    newBlockEntities.add(newInfo);
                } else {
                    // TODO: how?
                    // TODO: is this even possible (maybe very edge-case), because when chunk is loaded it's put into existingBlockEntities, so should be at unload as well right?
                    Logger.severe("BlockEntityManager: Unable to save block entity '" + dataParts[3] + "' at " + blockPos);
                }
            }

            chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY, newBlockEntities.toArray(new String[0]));
        }
    }

    @ApiStatus.Internal
    public static void removeBlockEntityFromChunk(@NotNull Location pos) {
        Chunk chunk = pos.getChunk();
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY)) {
            String[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY);
            if (blockEntities == null) return;

            List<String> newBlockEntities = new ArrayList<>();
            for (String info : blockEntities) {
                String[] parts = info.split(";");
                if (parts.length < 1) return;

                String data = parts[0];
                String[] dataParts = data.split(",");
                int blockX = Integer.parseInt(dataParts[0]);
                int blockY = Integer.parseInt(dataParts[1]);
                int blockZ = Integer.parseInt(dataParts[2]);
                if (pos.getBlockX() == blockX && pos.getBlockY() == blockY && pos.getBlockZ() == blockZ) {
                    if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully removed block entity '" + dataParts[3] + "' at " + pos);
                    continue;
                }
                newBlockEntities.add(info);
            }

            chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY, newBlockEntities.toArray(new String[0]));
        }
    }

    @ApiStatus.Internal
    public static void loadBlockEntitiesFromChunk(@NotNull Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY)) {
            String[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY);
            if (blockEntities == null) return;

            for (String info : blockEntities) {
                String[] parts = info.split(";");
                if (parts.length < 1) return;

                String data = parts[0];
                String[] dataParts = data.split(",");
                int blockX = Integer.parseInt(dataParts[0]);
                int blockY = Integer.parseInt(dataParts[1]);
                int blockZ = Integer.parseInt(dataParts[2]);
                NamespacedKey entityKey = NamespacedKey.fromString(dataParts[3]);
                if (entityKey == null) continue;

                Map<String, String> customData = new HashMap<>();
                if (parts.length > 1) {
                    String custom = parts[1];
                    String[] customParts = custom.split(",");
                    for (String part : customParts) {
                        String[] partParts = part.split(":");
                        if (partParts.length != 2) continue;
                        customData.put(partParts[0], partParts[1]);
                    }
                }

                IglooBlockEntityType<?> entityType = IglooRegistry.BLOCK_ENTITY_TYPE.get(entityKey);
                if (entityType == null) continue;

                // TODO: apparently chunk#getBlock takes the chunk relative coordinates to the block, meaning we could downsize the x and z coord when storing
                //Location blockPos = chunk.getBlock(blockX, blockY, blockZ).getLocation();
                Location blockPos = chunk.getWorld().getBlockAt(blockX, blockY, blockZ).getLocation();
                IglooBlockEntity blockEntity = entityType.instantiate(blockPos);
                if (blockEntity == null) continue;

                blockEntity.loadChunkData(customData);
                INSTANCE.existingBlockEntities.put(blockPos, new Pair<>(entityType, blockEntity));
                if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully loaded block entity '" + dataParts[3] + "' at " + blockPos);
            }
        }
    }

    private static void createTickTask() {
        // TODO: some warning/error is thrown when creating multiple and using async task (test)
        TaskManager.registerRepeatingTask("igloolib__BlockEntityManager_tick", () -> {
            INSTANCE.existingBlockEntities.forEach((pos, entity) -> {
                if (pos.getChunk().isLoaded()) {
                    entity.getSecond().tick(pos.getWorld(), pos, Objects.requireNonNull(pos.getWorld()).getBlockAt(pos));
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
