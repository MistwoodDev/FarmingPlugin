package me.munchii.igloolib.player;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.IgloolibConfig;
import me.munchii.igloolib.nms.IglooItemStack;
import me.munchii.igloolib.text.Text;
import me.munchii.igloolib.util.Chat;
import me.munchii.igloolib.util.UUIDCache;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class IglooPlayer {
    private static final UUIDCache<IglooPlayer> PLAYER_CACHE = new UUIDCache<>(2048);

    public static void clearPlayerCache() {
        PLAYER_CACHE.clear();
    }

    @NotNull
    public static IglooPlayer get(@NotNull CommandSender sender) {
        if (sender instanceof Player player) {
            return get(player);
        }

        return new IglooPlayer(sender);
    }

    @NotNull
    public static IglooPlayer get(@NotNull Player player) {
        return PLAYER_CACHE.getOrDefault(player.getUniqueId(), () -> new IglooPlayer(player));
    }

    @NotNull
    public static IglooPlayer get(@NotNull OfflinePlayer offlinePlayer) {
        return PLAYER_CACHE.getOrDefault(offlinePlayer.getUniqueId(), () -> new IglooPlayer(offlinePlayer));
    }

    @NotNull
    public static IglooPlayer get(@NotNull UUID uuid) {
        return PLAYER_CACHE.getOrDefault(uuid, () -> new IglooPlayer(uuid));
    }

    public static void removePlayer(Player player) {
        if (player != null) {
            PLAYER_CACHE.remove(player.getUniqueId());
        }
    }

    private final JavaPlugin instance = Igloolib.INSTANCE;

    @Nullable
    private final CommandSender commandSender;
    @Nullable
    private final Player player;
    private OfflinePlayer offlinePlayer;
    private final UUID uuid;

    private IglooPlayer(@Nullable CommandSender commandSender) {
        this.commandSender = commandSender;
        this.player = null;
        this.uuid = null;
    }

    private IglooPlayer(@NotNull Player player) {
        this.commandSender = player;
        this.player = player;
        this.offlinePlayer = player;
        this.uuid = player.getUniqueId();

        PLAYER_CACHE.put(uuid, this);
    }

    private IglooPlayer(@NotNull OfflinePlayer offlinePlayer) {
        this.commandSender = offlinePlayer.isOnline() ? offlinePlayer.getPlayer() : null;
        this.player = offlinePlayer.isOnline() ? offlinePlayer.getPlayer() : null;
        this.offlinePlayer = offlinePlayer;
        this.uuid = offlinePlayer.getUniqueId();
    }

    private IglooPlayer(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.commandSender = player;
        this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        this.uuid = uuid;
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return Objects.requireNonNull(commandSender).getEffectivePermissions();
    }

    @NotNull
    public PlayerInventory getInventory() {
        return Objects.requireNonNull(player).getInventory();
    }

    public InventoryView openInventory(Inventory inventory) {
        return Objects.requireNonNull(player).openInventory(inventory);
    }

    @NotNull
    public Location getLocation() {
        return Objects.requireNonNull(player).getLocation();
    }

    @NotNull
    public String getName() {
        return Objects.requireNonNull(player).getName();
    }

    @NotNull
    public String getDisplayName() {
        return Objects.requireNonNull(player).getDisplayName();
    }

    @Nullable
    public CommandSender getCommandSender() {
        return commandSender;
    }

    @NotNull
    public Player getPlayer() {
        return Objects.requireNonNull(player);
    }

    public boolean isPlayer() {
        return player != null;
    }

    @NotNull
    public OfflinePlayer getOfflinePlayer() {
        return Objects.requireNonNull(offlinePlayer);
    }

    public boolean isOfflinePlayer() {
        return offlinePlayer != null;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public boolean hasPermission(@Nullable String permission) {
        return permission == null || permission.isEmpty() || isOp() || commandSender.hasPermission(permission);
    }

    public boolean removePermission(@NotNull String permission) {
        for (PermissionAttachmentInfo permissionAttachmentInfo : getEffectivePermissions()) {
            if (permissionAttachmentInfo.getPermission().equals(permission) && permissionAttachmentInfo.getAttachment() != null) {
                Objects.requireNonNull(player).removeAttachment(permissionAttachmentInfo.getAttachment());
                break;
            }
        }

        Objects.requireNonNull(player).recalculatePermissions();
        return !player.hasPermission(permission);
    }

    public PermissionAttachment addPermission(@NotNull String permission) {
        return Objects.requireNonNull(player).addAttachment(instance, permission, true);
    }

    public boolean isOnline() {
        return player != null && player.isOnline();
    }

    public boolean isOp() {
        if (commandSender != null) {
            return commandSender.isOp();
        }

        if (uuid != null && offlinePlayer != null) {
            return offlinePlayer.isOp();
        }

        return false;
    }

    public Text getTranslatable(String key, Object... args) {
        return Text.translatableColor(player, key, args);
    }

    public void sendMessage(Text message) {
        message.send(Objects.requireNonNull(player));
    }

    public void sendMessage(String message) {
        sendMessage(new Text.Literal(Chat.color(message)));
    }

    public void sendTranslatable(String key, Object... args) {
        sendMessage(getTranslatable(key, args));
    }

    public void setGameMode(GameMode gameMode) {
        Objects.requireNonNull(player).setGameMode(gameMode);
    }

    public void teleport(Location destination) {
        Objects.requireNonNull(player).teleport(destination);
    }

    public void teleportTo(Player player) {
        Objects.requireNonNull(this.player).teleport(player);
    }

    @NotNull
    public World getWorld() {
        return Objects.requireNonNull(Objects.requireNonNull(player).getWorld());
    }

    public void closeInventory() {
        Objects.requireNonNull(player).closeInventory();
    }

    public Locale getLocale() {
        if (commandSender instanceof Player p) {
            return Locale.forLanguageTag(p.getLocale());
        }
        return Locale.forLanguageTag(IgloolibConfig.defaultLocale);
    }

    public void updateInventory() {
        //noinspection UnstableApiUsage
        Objects.requireNonNull(player).updateInventory();
    }

    public boolean performCommand(String command) {
        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(getPlayer(), command);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            return getPlayer().performCommand(event.getMessage().startsWith("/") ? event.getMessage().substring(1) : event.getMessage());
        }

        return true;
    }

    public void giveItem(IglooItemStack item) {
        getInventory().addItem(item.asBukkitStack());
        updateInventory();
    }
}
