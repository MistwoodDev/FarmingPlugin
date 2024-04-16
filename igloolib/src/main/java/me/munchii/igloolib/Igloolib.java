package me.munchii.igloolib;

import me.munchii.igloolib.block.BlockEntityManager;
import me.munchii.igloolib.block.DefaultBlockEntityListener;
import me.munchii.igloolib.block.IglooBlockEntity;
import me.munchii.igloolib.block.IglooBlockEntityType;
import me.munchii.igloolib.command.CommandManager;
import me.munchii.igloolib.command.IglooCommand;
import me.munchii.igloolib.command.IglooCommandGroup;
import me.munchii.igloolib.config.Configuration;
import me.munchii.igloolib.gui.inventory.DefaultWindowListener;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.screen.ScreenListener;
import me.munchii.igloolib.text.LocaleManager;
import me.munchii.igloolib.util.ListenerManager;
import me.munchii.igloolib.util.LocationUtil;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Supplier;

public final class Igloolib extends JavaPlugin {

    public static Igloolib INSTANCE = null;
    private static final ListenerManager LISTENERS = new ListenerManager();
    private static CommandManager commandManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        new Configuration(IgloolibConfig.class, this);

        registerListener(DefaultBlockEntityListener::new);
        registerListener(BlockEntityManager.ChunkListener::new);
        registerListener(DefaultWindowListener::new);
        registerListener(ScreenListener::new);

        commandManager = new CommandManager();
        commandManager.registerCommandGroup(IglooCommandGroup.create("igloo")
                .withCommand(IglooCommand.create("locale")
                        .withDescription("Locale tools")
                        .withUsage("/igloo locale <sub-command>")
                        .withPermission("igloolib.admin")
                        .withAction(ctx -> {
                            List<String> args = ctx.getArgs();
                            if (args.isEmpty()) return false;

                            if (Objects.equals(args.get(0), "reload")) {
                                LocaleManager.reload();
                                ctx.getPlayer().sendMessage("&7[igloolib] Locales successfully reloaded!");
                                return true;
                            } else if (Objects.equals(args.get(0), "langs")) {
                                Set<String> languages = LocaleManager.keySet();
                                final String language = languages.size() == 1 ? "language" : "languages";
                                final int loadedFiles = LocaleManager.getLoadedFiles();
                                final String file = loadedFiles == 1 ? "file" : "files";

                                ctx.getPlayer().sendMessage("&7[igloolib] Found " + languages.size() + " " + language + " registered across " + loadedFiles + " " + file + "!");
                                for (String key : languages) {
                                    ctx.getPlayer().sendMessage("&7 - " + key);
                                }
                                return true;
                            }

                            return false;
                        })
                        .withTabComplete((sender, args) -> {
                            if (args.size() == 2) return List.of("reload", "langs");

                            return new ArrayList<>();
                        })
                        .build()
                )
                .withCommand(IglooCommand.create("bem")
                        .withDescription("BlockEntityManager tools")
                        .withUsage("/igloo bem <sub-command>")
                        .withPermission("igloolib.admin")
                        .withAction(ctx -> {
                            List<String> args = ctx.getArgs();
                            if (args.isEmpty()) return false;

                            if (Objects.equals(args.get(0), "clearchunk")) {
                                BlockEntityManager.clearChunkData(ctx.getPlayer().getLocation().getChunk());
                                ctx.getPlayer().sendMessage("&7[igloolib] Chunk data cleared of block entities!");
                                return true;
                            } else if (Objects.equals(args.get(0), "clearworld")) {
                                Set<IglooBlockEntity> blockEntities = BlockEntityManager.getBlockEntities();
                                World world = ctx.getWorld();
                                for (IglooBlockEntity blockEntity : blockEntities) {
                                    if (Objects.equals(blockEntity.getWorld(), world)) {
                                        BlockEntityManager.removeBlockEntity(blockEntity.getPos());
                                    }
                                }
                                ctx.getPlayer().sendMessage("&7[igloolib] World cleared of block entities!");
                                return true;
                            } else if (Objects.equals(args.get(0), "clearall")) {
                                BlockEntityManager.clearBlockEntities();
                                ctx.getPlayer().sendMessage("&7[igloolib] All block entities cleared!");
                                return true;
                            } else if (Objects.equals(args.get(0), "listchunk")) {
                                Set<IglooBlockEntity> blockEntities = new HashSet<>();
                                Chunk chunk = ctx.getPlayer().getLocation().getChunk();
                                for (Location pos : LocationUtil.getLocationsInChunk(chunk)) {
                                    if (BlockEntityManager.isBlockEntityAt(pos)) {
                                        blockEntities.add(BlockEntityManager.getBlockEntity(pos));
                                    }
                                }

                                String entity = blockEntities.size() == 1 ? "entity" : "entities";
                                ctx.getPlayer().sendMessage("&7[igloolib] Found " + blockEntities.size() + " block " + entity + " in chunk (" + chunk.getX() + ", " + chunk.getZ() + ")!");
                                for (IglooBlockEntity blockEntity : blockEntities) {
                                    ctx.getPlayer().sendMessage("&7 - '" + IglooBlockEntityType.getId(blockEntity.getType()).toString() + "' @ " + LocationUtil.toSimpleString(blockEntity.getPos()) + "!");
                                }
                                return true;
                            } else if (Objects.equals(args.get(0), "listworld")) {
                                Set<IglooBlockEntity> blockEntities = new HashSet<>();
                                for (IglooBlockEntity blockEntity : BlockEntityManager.getBlockEntities()) {
                                    if (Objects.equals(blockEntity.getWorld(), ctx.getWorld())) {
                                        blockEntities.add(blockEntity);
                                    }
                                }

                                String entity = blockEntities.size() == 1 ? "entity" : "entities";
                                ctx.getPlayer().sendMessage("&7[igloolib] Found " + blockEntities.size() + " block " + entity + " in the world!");
                                for (IglooBlockEntity blockEntity : blockEntities) {
                                    ctx.getPlayer().sendMessage("&7 - '" + IglooBlockEntityType.getId(blockEntity.getType()).toString() + "' @ " + LocationUtil.toSimpleString(blockEntity.getPos()) + "!");
                                }
                                return true;
                            } else if (Objects.equals(args.get(0), "listall")) {
                                Set<IglooBlockEntity> blockEntities = BlockEntityManager.getBlockEntities();
                                String entity = blockEntities.size() == 1 ? "entity" : "entities";
                                ctx.getPlayer().sendMessage("&7[igloolib] Found " + blockEntities.size() + " block " + entity + "!");
                                for (IglooBlockEntity blockEntity : blockEntities) {
                                    ctx.getPlayer().sendMessage("&7 - '" + IglooBlockEntityType.getId(blockEntity.getType()).toString() + "' @ " + LocationUtil.toSimpleString(blockEntity.getPos()) + "!");
                                }
                                return true;
                            } else if (Objects.equals(args.get(0), "count")) {
                                final int size = BlockEntityManager.getBlockEntities().size();
                                String entity = size == 1 ? "entity" : "entities";
                                ctx.getPlayer().sendMessage("&7[igloolib] Found " + size + " block " + entity + "!");
                                return true;
                            } else if (Objects.equals(args.get(0), "cleartype")) {
                                if (args.size() < 3) return false;
                                String scope = args.get(1);
                                String blockEntityType = args.get(2);
                                NamespacedKey blockEntityKey = NamespacedKey.fromString(blockEntityType);
                                if (blockEntityKey == null) return false;
                                IglooBlockEntityType<?> iglooBlockEntityType = IglooRegistry.BLOCK_ENTITY_TYPE.get(blockEntityKey);

                                if (scope.equals("chunk")) {

                                } else if (scope.equals("world")) {

                                } else if (scope.equals("all")) {

                                } else return false;

                                ctx.getPlayer().sendMessage("&7[igloolib] Cleared all block entities of type '" + blockEntityType + "'!");
                                return true;
                            }

                            return false;
                        })
                        .withTabComplete((sender, args) -> {
                            if (args.size() == 2) return List.of("clearchunk", "clearworld", "clearall", "listchunk", "listworld", "listall", "count", "cleartype");
                            else if (args.size() > 1 && Objects.equals(args.get(1), "cleartype")) {
                                if (args.size() == 3) return List.of("chunk", "world", "all");
                                else if (args.size() == 4)
                                    return IglooRegistry.BLOCK_ENTITY_TYPE.keySet().stream().map(NamespacedKey::toString).toList();
                            }

                            return new ArrayList<>();
                        })
                        .build()
                ).create());
        commandManager.enable();

        getLogger().info("igloolib initialized");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Listener registerListener(Supplier<Listener> listenerSupplier) {
        return LISTENERS.register(listenerSupplier);
    }
}
