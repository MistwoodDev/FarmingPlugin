package me.munchii.igloolib.nms;

import me.munchii.igloolib.util.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class NbtCompound {
    private NBTTagCompound compound;

    public NbtCompound() {
        this(new NBTTagCompound());
    }

    public NbtCompound(NBTTagCompound compound) {
        this.compound = compound;
    }

    public static NbtCompound of(ItemStack stack) {
        return new NbtCompound(NBTUtil.getItemNBT(stack));
    }

    public static NbtCompound of(org.bukkit.inventory.ItemStack stack) {
        return new NbtCompound(NBTUtil.getItemNBT(stack));
    }

    public static NbtCompound of(NbtCompound compound) {
        NbtCompound nbt = new NbtCompound();
        nbt.compound = NBTUtil.nbtCopyFrom(nbt.compound, compound.compound);
        return nbt;
    }

    public void putCompound(String key, NbtCompound compound) {
        NBTUtil.nbtPutCompound(this.compound, key, compound.compound);
    }

    public void putCompound(String key, NBTTagCompound compound) {
        NBTUtil.nbtPutCompound(this.compound, key, compound);
    }

    public void putByte(String key, byte value) {
        NBTUtil.nbtPutByte(compound, key, value);
    }

    public void putShort(String key, short value) {
        NBTUtil.nbtPutShort(compound, key, value);
    }

    public void putInt(String key, int value) {
        NBTUtil.nbtPutInt(compound, key, value);
    }

    public void putLong(String key, long value) {
        NBTUtil.nbtPutLong(compound, key, value);
    }

    public void putUUID(String key, UUID value) {
        NBTUtil.nbtPutUUID(compound, key, value);
    }

    public void putFloat(String key, float value) {
        NBTUtil.nbtPutFloat(compound, key, value);
    }

    public void putDouble(String key, double value) {
        NBTUtil.nbtPutDouble(compound, key, value);
    }

    public void putString(String key, String value) {
        NBTUtil.nbtPutString(compound, key, value);
    }

    public void putByteArray(String key, byte[] value) {
        NBTUtil.nbtPutByteArray(compound, key, value);
    }

    public void putByteArray(String key, List<Byte> value) {
        NBTUtil.nbtPutByteArray(compound, key, value);
    }

    public void putIntArray(String key, int[] value) {
        NBTUtil.nbtPutIntArray(compound, key, value);
    }

    public void putIntArray(String key, List<Integer> value) {
        NBTUtil.nbtPutIntArray(compound, key, value);
    }

    public void putLongArray(String key, long[] value) {
        NBTUtil.nbtPutLongArray(compound, key, value);
    }

    public void putLongArray(String key, List<Long> value) {
        NBTUtil.nbtPutLongArray(compound, key, value);
    }

    public void putBoolean(String key, boolean value) {
        NBTUtil.nbtPutBoolean(compound, key, value);
    }

    public NbtCompound getCompound(String key) {
        return new NbtCompound(NBTUtil.nbtGetCompound(compound, key));
    }

    public NBTTagCompound getRawCompound(String key) {
        return NBTUtil.nbtGetCompound(compound, key);
    }

    public byte getByte(String key) {
        return NBTUtil.nbtGetByte(compound, key);
    }

    public short getShort(String key) {
        return NBTUtil.nbtGetShort(compound, key);
    }

    public int getInt(String key) {
        return NBTUtil.nbtGetInt(compound, key);
    }

    public long getLong(String key) {
        return NBTUtil.nbtGetLong(compound, key);
    }

    public UUID getUUID(String key) {
        return NBTUtil.nbtGetUUID(compound, key);
    }

    public float getFloat(String key) {
        return NBTUtil.nbtGetFloat(compound, key);
    }

    public double getDouble(String key) {
        return NBTUtil.nbtGetDouble(compound, key);
    }

    public String getString(String key) {
        return NBTUtil.nbtGetString(compound, key);
    }

    public byte[] getByteArray(String key) {
        return NBTUtil.nbtGetByteArray(compound, key);
    }

    public int[] getIntArray(String key) {
        return NBTUtil.nbtGetIntArray(compound, key);
    }

    public long[] getLongArray(String key) {
        return NBTUtil.nbtGetLongArray(compound, key);
    }

    public boolean getBoolean(String key) {
        return NBTUtil.nbtGetBoolean(compound, key);
    }

    public boolean contains(String key) {
        return NBTUtil.nbtContainsKey(compound, key);
    }

    public void remove(String key) {
        NBTUtil.nbtRemoveEntry(compound, key);
    }

    public int size() {
        return NBTUtil.nbtGetSize(compound);
    }

    public Set<String> keySet() {
        return NBTUtil.nbtGetKeySet(compound);
    }

    public void copyFrom(NbtCompound compound) {
        this.compound = NBTUtil.nbtCopyFrom(this.compound, compound.compound);
    }

    public void copyFrom(NBTTagCompound compound) {
        this.compound = NBTUtil.nbtCopyFrom(this.compound, compound);
    }

    public boolean isEmpty() {
        return NBTUtil.nbtIsEmpty(compound);
    }

    NBTTagCompound getCompound() {
        return compound;
    }
}
