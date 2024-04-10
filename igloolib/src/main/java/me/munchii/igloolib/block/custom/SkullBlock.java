package me.munchii.igloolib.block.custom;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.munchii.igloolib.block.IglooBlock;
import me.munchii.igloolib.block.IglooBlockItem;
import me.munchii.igloolib.player.IglooPlayer;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class SkullBlock extends IglooBlock {
    @Nullable
    private UUID uuid = null;
    @Nullable
    private String base64 = null;

    private SkullBlock(@NotNull UUID uuid) {
        super(Material.PLAYER_HEAD);
        this.uuid = uuid;
    }

    private SkullBlock(@NotNull String base64) {
        super(Material.PLAYER_HEAD);
        this.base64 = base64;
    }

    public static SkullBlock of(@NotNull IglooPlayer player) {
        return of(player.getUniqueId());
    }

    public static SkullBlock of(@NotNull UUID uuid) {
        return new SkullBlock(uuid);
    }

    public static SkullBlock of(@NotNull String base64) {
        return new SkullBlock(base64);
    }

    @Override
    public @NotNull IglooBlockItem asItem() {
        return new SkullBlockItem(this);
    }

    public static class SkullBlockItem extends IglooBlockItem {
        private final SkullBlock skullBlock;

        private static Field blockProfileField;
        private static Method metaSetProfileMethod;
        private static Field metaProfileField;

        public SkullBlockItem(SkullBlock block) {
            super(block);
            skullBlock = block;
        }

        @Override
        public ItemStack getItem() {
            ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            if (skullBlock.uuid != null) {
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(skullBlock.uuid));
            } else if (skullBlock.base64 != null) {
                mutateItemMeta(meta, skullBlock.base64);
            }
            stack.setItemMeta(meta);

            return stack;
        }

        private static GameProfile makeProfile(String base64) {
            UUID uuid = new UUID(
                    base64.substring(base64.length() - 20).hashCode(),
                    base64.substring(base64.length() - 10).hashCode()
            );
            GameProfile profile = new GameProfile(uuid, "igloo");
            profile.getProperties().put("textures", new Property("textures", base64));
            return profile;
        }

        private static void mutateItemMeta(SkullMeta meta, String base64) {
            try {
                if (metaSetProfileMethod == null) {
                    metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                    metaSetProfileMethod.setAccessible(true);
                }
                metaSetProfileMethod.invoke(meta, makeProfile(base64));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                try {
                    if (metaProfileField == null) {
                        metaProfileField = meta.getClass().getDeclaredField("profile");
                        metaProfileField.setAccessible(true);
                    }
                    metaProfileField.set(meta, makeProfile(base64));
                } catch (NoSuchFieldException | IllegalAccessException e2) {
                    Logger.severe("Failed to set profile field: ");
                    Logger.severe(e2.getStackTrace());
                }
            }
        }
    }
}
