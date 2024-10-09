package me.munchii.igloolib;

import me.munchii.igloolib.block.entity.BlockEntityManager;
import me.munchii.igloolib.block.entity.DefaultBlockEntityListener;
import me.munchii.igloolib.block.entity.IglooBlockEntity;
import me.munchii.igloolib.block.entity.IglooBlockEntityType;
import me.munchii.igloolib.command.CommandManager;
import me.munchii.igloolib.command.IglooCommand;
import me.munchii.igloolib.command.IglooCommandGroup;
import me.munchii.igloolib.config.Configuration;
import me.munchii.igloolib.gui.inventory.DefaultWindowListener;
import me.munchii.igloolib.gui.window.FurnaceWindow;
import me.munchii.igloolib.nms.IglooBlockDisplay;
import me.munchii.igloolib.registry.IglooRegistry;
import me.munchii.igloolib.screen.ScreenListener;
import me.munchii.igloolib.text.LocaleManager;
import me.munchii.igloolib.util.*;
import org.bukkit.*;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.craftbukkit.v1_20_R2.inventory.util.CraftTileInventoryConverter;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

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
        commandManager.registerCommand(IglooCommand.create("test")
                .withAction(ctx -> {
                    /*Location pos = ctx.getPlayer().getLocation().add(0, 5, 0);
                    ArmorStand armorStand = (ArmorStand) pos.getWorld().spawnEntity(pos, EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setInvulnerable(true);
                    armorStand.setGravity(false);
                    armorStand.getEquipment().setHelmet(new ItemStack(Material.LECTERN, 1));

                    ArmorStand armorStand2 = (ArmorStand) pos.getWorld().spawnEntity(pos.subtract(0, 1, 0), EntityType.ARMOR_STAND);
                    armorStand2.setVisible(false);
                    armorStand2.setInvulnerable(true);
                    armorStand2.setGravity(false);
                    armorStand2.getEquipment().setHelmet(new ItemStack(Material.SMOOTH_SANDSTONE, 1));

                    ArmorStand armorStand3 = (ArmorStand) pos.getWorld().spawnEntity(pos.add(5, 0, 5), EntityType.ARMOR_STAND);
                    armorStand3.setVisible(false);
                    armorStand3.setInvulnerable(true);
                    armorStand3.setGravity(false);
                    armorStand3.setCustomName(Chat.color("&a&lYEET"));
                    armorStand3.setCustomNameVisible(true);

                    BlockDisplay blockDisplay = (BlockDisplay) pos.getWorld().spawnEntity(pos.add(10, 0, 10), EntityType.BLOCK_DISPLAY);
                    blockDisplay.setBlock(pos.getWorld().getBlockData(pos.subtract(0, 50, 0)));
                    blockDisplay.setBrightness(new Display.Brightness(15, 15));
                    blockDisplay.setDisplayWidth(0.5f);
                    blockDisplay.setDisplayHeight(0.5f);
                    blockDisplay.setGravity(false);
                    blockDisplay.setInvulnerable(true);
                    blockDisplay.setGlowing(true);
                    blockDisplay.setGlowColorOverride(Color.PURPLE);
                    blockDisplay.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(0.5f), new AxisAngle4f()));

                    IglooBlockDisplay blockDisplay2 = pos.getWorld().spawn(pos.add(10, 0, 15), IglooBlockDisplay.class);
                    blockDisplay2.setBlock(pos.getWorld().getBlockData(pos.subtract(0, 50, 0)));
                    blockDisplay2.setGravity(false);
                    blockDisplay2.setInvulnerable(true);

                    TaskManager.runAnonymousDelayedTask(() -> {
                        armorStand.remove();
                        armorStand2.remove();
                        armorStand3.remove();
                        blockDisplay.remove();
                        blockDisplay2.remove();
                    }, 20, TimeUnit.SECOND);*/

                    FurnaceWindow furnaceWindow = new FurnaceWindow("yeet", window -> {
                        if (!window.getInputSlot().isEmpty() && !window.getFuelSlot().isEmpty()) {
                        }
                    });
                    InventoryView view = furnaceWindow.open(ctx.getPlayer());
                    view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_FUEL, 600); // total burn time
                    view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_SMELTING, 600); // total cook time
                    view.setProperty(InventoryView.Property.BURN_TIME, 300); // current burn time
                    view.setProperty(InventoryView.Property.COOK_TIME, 300); // current cook time
                    //((Player) view.getPlayer()).updateInventory();

                    TaskManager.runAnonymousDelayedTask(() -> {
                        Logger.severe("input=" + furnaceWindow.getInputSlot());
                        Logger.severe("fuel=" + furnaceWindow.getFuelSlot());
                        Logger.severe("output=" + furnaceWindow.getOutputSlot());
                    }, 20, TimeUnit.SECOND);

                    return true;
                })
                .build());
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
