package me.munchii.igloolib.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class NBTUtil {
    public static net.minecraft.world.item.ItemStack getNMSStack(ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack);
    }

    public static ItemStack getBukkitStack(net.minecraft.world.item.ItemStack stack) {
        return CraftItemStack.asBukkitCopy(stack);
    }

    public static NBTTagCompound getItemNBT(ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        return nmsStack.getOrCreateTag();
        //return nmsStack.w();
    }

    public static NBTTagCompound getItemNBT(net.minecraft.world.item.ItemStack stack) {
        return stack.getOrCreateTag();
        //return stack.w();
    }

    public static void putItemNBT(ItemStack stack, NBTTagCompound nbt) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        nmsStack.setTag(nbt);
        //nmsStack.c(nbt);
    }

    public static void putItemNBT(net.minecraft.world.item.ItemStack stack, NBTTagCompound nbt) {
        stack.setTag(nbt);
        //stack.c(nbt);
    }

    public static void nbtPutCompound(NBTTagCompound nbt, String key, NBTTagCompound compound) {
        nbt.put(key, compound);
        //nbt.a(key, compound);
    }

    public static void nbtPutByte(NBTTagCompound nbt, String key, byte value) {
        nbt.putByte(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutShort(NBTTagCompound nbt, String key, short value) {
        nbt.putShort(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutInt(NBTTagCompound nbt, String key, int value) {
        nbt.putInt(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutLong(NBTTagCompound nbt, String key, long value) {
        nbt.putLong(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutUUID(NBTTagCompound nbt, String key, UUID value) {
        nbt.putUUID(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutFloat(NBTTagCompound nbt, String key, float value) {
        nbt.putFloat(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutDouble(NBTTagCompound nbt, String key, double value) {
        nbt.putDouble(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutString(NBTTagCompound nbt, String key, String value) {
        nbt.putString(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutByteArray(NBTTagCompound nbt, String key, byte[] value) {
        nbt.putByteArray(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutByteArray(NBTTagCompound nbt, String key, List<Byte> value) {
        nbt.putByteArray(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutIntArray(NBTTagCompound nbt, String key, int[] value) {
        nbt.putIntArray(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutIntArray(NBTTagCompound nbt, String key, List<Integer> value) {
        nbt.putIntArray(key, value);
        //nbt.b(key, value);
    }

    public static void nbtPutLongArray(NBTTagCompound nbt, String key, long[] value) {
        nbt.putLongArray(key, value);
        //nbt.a(key, value);
    }

    public static void nbtPutLongArray(NBTTagCompound nbt, String key, List<Long> value) {
        nbt.putLongArray(key, value);
        //nbt.c(key, value);
    }

    public static void nbtPutBoolean(NBTTagCompound nbt, String key, boolean value) {
        nbt.putBoolean(key, value);
        //nbt.a(key, value);
    }

    public static NBTTagCompound nbtGetCompound(NBTTagCompound nbt, String key) {
        return nbt.getCompound(key);
        //return nbt.p(key);
    }

    public static byte nbtGetByte(NBTTagCompound nbt, String key) {
        return nbt.getByte(key);
        //return nbt.f(key);
    }

    public static short nbtGetShort(NBTTagCompound nbt, String key) {
        return nbt.getShort(key);
        //return nbt.g(key);
    }

    public static int nbtGetInt(NBTTagCompound nbt, String key) {
        return nbt.getInt(key);
        //return nbt.h(key);
    }

    public static long nbtGetLong(NBTTagCompound nbt, String key) {
        return nbt.getLong(key);
        //return nbt.i(key);
    }

    public static UUID nbtGetUUID(NBTTagCompound nbt, String key) {
        return nbt.getUUID(key);
        //return nbt.a(key);
    }

    public static float nbtGetFloat(NBTTagCompound nbt, String key) {
        return nbt.getFloat(key);
        //return nbt.j(key);
    }

    public static double nbtGetDouble(NBTTagCompound nbt, String key) {
        return nbt.getDouble(key);
        //return nbt.k(key);
    }

    public static String nbtGetString(NBTTagCompound nbt, String key) {
        return nbt.getString(key);
        //return nbt.l(key);
    }

    public static byte[] nbtGetByteArray(NBTTagCompound nbt, String key) {
        return nbt.getByteArray(key);
        //return nbt.m(key);
    }

    public static int[] nbtGetIntArray(NBTTagCompound nbt, String key) {
        return nbt.getIntArray(key);
        //return nbt.n(key);
    }

    public static long[] nbtGetLongArray(NBTTagCompound nbt, String key) {
        return nbt.getLongArray(key);
        //return nbt.o(key);
    }

    public static boolean nbtGetBoolean(NBTTagCompound nbt, String key) {
        return nbt.getBoolean(key);
        //return nbt.q(key);
    }

    public static boolean nbtContainsKey(NBTTagCompound nbt, String key) {
        return nbt.contains(key);
        //return nbt.e(key);
    }

    public static Optional<NBTBase> nbtGetBase(NBTTagCompound nbt, String key) {
        return nbtContainsKey(nbt, key) ? Optional.ofNullable(nbt.get(key)) : Optional.empty();
        //return nbtContainsKey(nbt, key) ? Optional.ofNullable(nbt.c(key)) : Optional.empty();
    }

    public static int nbtGetSize(NBTTagCompound nbt) {
        return nbt.size();
        //return nbt.f();
    }

    public static Set<String> nbtGetKeySet(NBTTagCompound nbt) {
        return nbt.getAllKeys();
        //return nbt.e();
    }

    public static void nbtRemoveEntry(NBTTagCompound nbt, String key) {
        nbt.remove(key);
        //nbt.r(key);
    }

    public static boolean nbtIsEmpty(NBTTagCompound nbt) {
        return nbt.isEmpty();
        //return nbt.g();
    }

    public static NBTTagCompound nbtCopyFrom(NBTTagCompound nbt, NBTTagCompound target) {
        return nbt.merge(target);
        //return nbt.a(target);
    }
}
