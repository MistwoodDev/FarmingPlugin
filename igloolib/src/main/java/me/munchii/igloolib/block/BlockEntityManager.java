package me.munchii.igloolib.block;

import com.mojang.datafixers.util.Pair;
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

    static void clearBlockEntities() {
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

        /*NbtCompound nbt = new NbtCompound();
        NbtCompound blockEntityNbt = new NbtCompound();
        blockEntity.writeNbt(blockEntityNbt);
        nbt.putIntArray("blockEntityPos", new int[] { pos.getBlockX(), pos.getBlockY(), pos.getBlockZ() });
        nbt.putString("blockEntityType", registryKey.toString());
        nbt.putCompound("blockEntityData", blockEntityNbt);
        CraftPersistentDataContainer pdc = nbt.toPersistentDataContainer();*/
        CraftPersistentDataContainer pdc = blockEntity.createNbtWithIdentifyingData().toPersistentDataContainer();

        PersistentDataContainer[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY);
        if (blockEntities == null) {
            blockEntities = new PersistentDataContainer[0];
        }

        List<PersistentDataContainer> blockEntitiesList = new ArrayList<>(Arrays.stream(blockEntities).toList());
        blockEntitiesList.add(pdc);

        chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY, blockEntitiesList.toArray(new PersistentDataContainer[0]));

        /*String info = String.join(",",
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
            chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY);
            if (blockEntities == null) {
                blockEntities = new String[0];
            }
        }

        List<String> blockEntitiesList = new ArrayList<>(Arrays.stream(blockEntities).toList());
        blockEntitiesList.add(info);

        chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY, blockEntitiesList.toArray(new String[0]));*/
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
                /*int[] pos = nbt.getIntArray("blockEntityPos");
                Location blockPos = chunk.getWorld().getBlockAt(pos[0], pos[1], pos[2]).getLocation();*/
                Location blockPos = chunk.getWorld().getBlockAt(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")).getLocation();

                if (INSTANCE.existingBlockEntities.containsKey(blockPos)) {
                    IglooBlockEntity blockEntity = INSTANCE.existingBlockEntities.get(blockPos);

                    /*if (nbt.contains("blockEntityData")) {
                        nbt.remove("blockEntityData");
                    }

                    NbtCompound blockEntityNbt = new NbtCompound();
                    blockEntityPair.getSecond().writeNbt(blockEntityNbt);
                    nbt.putCompound("blockEntityData", blockEntityNbt);*/
                    if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully saved block entity '" + nbt.getString("id") + "' at " + blockPos);
                    /*newBlockEntities.add(nbt.toPersistentDataContainer());*/
                    newBlockEntities.add(blockEntity.createNbtWithIdentifyingData().toPersistentDataContainer());
                } else {
                    // TODO: how?
                    // TODO: is this even possible (maybe very edge-case), because when chunk is loaded it's put into existingBlockEntities, so should be at unload as well right?
                    Logger.severe("BlockEntityManager: Unable to save block entity '" + nbt.getString("id") + "' at " + blockPos);
                }
            }

            chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY, newBlockEntities.toArray(new PersistentDataContainer[0]));
        }

        /*if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY)) {
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
        }*/
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
                /*int[] entityPos = nbt.getIntArray("blockEntityPos");
                if (pos.getBlockX() == entityPos[0] && pos.getBlockY() == entityPos[1] && pos.getBlockZ() == entityPos[2]) {*/
                if (pos.getBlockX() == nbt.getInt("x") && pos.getBlockY() == nbt.getInt("y") && pos.getBlockZ() == nbt.getInt("z")) {
                    if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully removed block entity '" + nbt.getString("id") + "' at " + pos);
                    continue;
                }

                newBlockEntities.add(pdc);
            }

            chunk.getPersistentDataContainer().set(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY, newBlockEntities.toArray(new PersistentDataContainer[0]));
        }

        /*if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY)) {
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
        }*/
    }

    @ApiStatus.Internal
    public static void loadBlockEntitiesFromChunk(@NotNull Chunk chunk) {
        if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY)) {
            PersistentDataContainer[] blockEntities = chunk.getPersistentDataContainer().get(BLOCK_ENTITIES_KEY, PersistentDataType.TAG_CONTAINER_ARRAY);
            if (blockEntities == null) return;

            for (PersistentDataContainer pdc : blockEntities) {
                CraftPersistentDataContainer cpdc = (CraftPersistentDataContainer) pdc;
                NbtCompound nbt = new NbtCompound(cpdc.toTagCompound());

                /*int[] entityPos = nbt.getIntArray("blockEntityPos");
                Location blockPos = chunk.getWorld().getBlockAt(entityPos[0], entityPos[1], entityPos[2]).getLocation();

                NamespacedKey entityTypeKey = NamespacedKey.fromString(nbt.getString("blockEntityType"));
                if (entityTypeKey == null) return;
                IglooBlockEntityType<?> blockEntityType = IglooRegistry.BLOCK_ENTITY_TYPE.get(entityTypeKey);
                if (blockEntityType == null) return;

                NbtCompound blockEntityData = nbt.getCompound("blockEntityData");

                IglooBlockEntity blockEntity = blockEntityType.instantiate(blockPos);
                if (blockEntity == null) return;

                blockEntity.readNbt(blockEntityData);*/

                Location blockPos = chunk.getWorld().getBlockAt(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")).getLocation();
                IglooBlockEntity blockEntity = IglooBlockEntity.createFromNbt(blockPos, nbt);
                if (blockEntity == null) {
                    Logger.severe("BlockEntityManager: Failed to load block entity of id '" + nbt.getString("id") + "' because of previous errors!");
                } else {
                    INSTANCE.existingBlockEntities.put(blockPos, blockEntity);
                    if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully loaded block entity '" + nbt.getString("blockEntityType") + "' at " + blockPos);
                }

                /*INSTANCE.existingBlockEntities.put(blockPos, new Pair<>(blockEntityType, blockEntity));
                if (IgloolibConfig.verbose) Logger.info("BlockEntityManager: Successfully loaded block entity '" + nbt.getString("blockEntityType") + "' at " + blockPos);*/
            }
        }

        /*if (chunk.getPersistentDataContainer().has(BLOCK_ENTITIES_KEY, IglooPersistentDataType.STRING_ARRAY)) {
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
        }*/
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
