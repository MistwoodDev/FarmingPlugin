package me.munchii.igloolib.nms;

import me.munchii.igloolib.Igloolib;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R2.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_20_R2.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("DataFlowIssue")
public final class PdcCompound {
    private final PersistentDataContainer compound;

    public PdcCompound() {
        this(new CraftPersistentDataContainer(new CraftPersistentDataTypeRegistry()));
    }

    public PdcCompound(PersistentDataContainer compound) {
        this.compound = compound;
    }

    public void putCompound(String key, PdcCompound compound) {
        this.compound.set(makeKey(key), PersistentDataType.TAG_CONTAINER, compound.compound);
    }

    public void putByte(String key, byte value) {
        this.compound.set(makeKey(key), PersistentDataType.BYTE, value);
    }

    public void putShort(String key, short value) {
        this.compound.set(makeKey(key), PersistentDataType.SHORT, value);
    }

    public void putInt(String key, int value) {
        this.compound.set(makeKey(key), PersistentDataType.INTEGER, value);
    }

    public void putLong(String key, long value) {
        this.compound.set(makeKey(key), PersistentDataType.LONG, value);
    }

    public void putUUID(String key, UUID value) {
        throw new RuntimeException("Not implemented yet");
    }

    public void putFloat(String key, float value) {
        this.compound.set(makeKey(key), PersistentDataType.FLOAT, value);
    }

    public void putDouble(String key, double value) {
        this.compound.set(makeKey(key), PersistentDataType.DOUBLE, value);
    }

    public void putString(String key, String value) {
        this.compound.set(makeKey(key), PersistentDataType.STRING, value);
    }

    public void putByteArray(String key, byte[] value) {
        this.compound.set(makeKey(key), PersistentDataType.BYTE_ARRAY, value);
    }

    public void putByteArray(String key, List<Byte> value) {
        this.compound.set(makeKey(key), PersistentDataType.BYTE_ARRAY, ArrayUtils.toPrimitive(value.toArray(new Byte[0])));
    }

    public void putIntArray(String key, int[] value) {
        this.compound.set(makeKey(key), PersistentDataType.INTEGER_ARRAY, value);
    }

    public void putIntArray(String key, List<Integer> value) {
        this.compound.set(makeKey(key), PersistentDataType.INTEGER_ARRAY, ArrayUtils.toPrimitive(value.toArray(new Integer[0])));
    }

    public void putLongArray(String key, long[] value) {
        this.compound.set(makeKey(key), PersistentDataType.LONG_ARRAY, value);
    }

    public void putLongArray(String key, List<Long> value) {
        this.compound.set(makeKey(key), PersistentDataType.LONG_ARRAY, ArrayUtils.toPrimitive(value.toArray(new Long[0])));
    }

    public void putBoolean(String key, boolean value) {
        this.compound.set(makeKey(key), PersistentDataType.BOOLEAN, value);
    }

    public PdcCompound getCompound(String key) {
        return new PdcCompound(this.compound.get(makeKey(key), PersistentDataType.TAG_CONTAINER));
    }

    public byte getByte(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.BYTE);
    }

    public short getShort(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.SHORT);
    }

    public int getInt(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.INTEGER);
    }

    public long getLong(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.LONG);
    }

    public UUID getUUID(String key) {
        return null;
    }

    public float getFloat(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.FLOAT);
    }

    public double getDouble(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.DOUBLE);
    }

    public String getString(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.STRING);
    }

    public byte[] getByteArray(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.BYTE_ARRAY);
    }

    public int[] getIntArray(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.INTEGER_ARRAY);
    }

    public long[] getLongArray(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.LONG_ARRAY);
    }

    public boolean getBoolean(String key) {
        return this.compound.get(makeKey(key), PersistentDataType.BOOLEAN);
    }

    public boolean contains(String key) {
        return this.compound.getKeys().contains(makeKey(key));
    }

    public boolean contains(String key, PersistentDataType<?, ?> type) {
        return this.compound.has(makeKey(key), type);
    }

    
    public void remove(String key) {
        this.compound.remove(makeKey(key));
    }

    
    public int size() {
        return this.compound.getKeys().size();
    }

    public Set<String> keySet() {
        return this.compound.getKeys().stream().map(NamespacedKey::getKey).collect(Collectors.toSet());
    }

    public Set<NamespacedKey> fullKeySet() {
        return this.compound.getKeys();
    }

    public boolean isEmpty() {
        return this.compound.isEmpty();
    }

    public PersistentDataContainer getCompound() {
        return compound;
    }

    @NotNull
    private static NamespacedKey makeKey(String key) {
        return new NamespacedKey(Igloolib.INSTANCE, key);
    }

    public void copyInto(@NotNull ItemMeta meta) {
        CraftPersistentDataContainer cpdc = (CraftPersistentDataContainer) compound;
        ((CraftPersistentDataContainer) meta.getPersistentDataContainer()).putAll(cpdc.getRaw());
    }

    public CraftPersistentDataContainer toPersistentDataContainer() {
        return (CraftPersistentDataContainer) compound;
    }
    
    public NbtCompound toNbtCompound() {
        return new NbtCompound(((CraftPersistentDataContainer) compound).toTagCompound());
    }
}
