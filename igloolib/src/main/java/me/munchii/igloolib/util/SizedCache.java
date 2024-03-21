package me.munchii.igloolib.util;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class SizedCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public SizedCache(int maxSize) {
        super(maxSize + 2, 1F);

        this.maxSize = maxSize;
    }

    public Optional<V> getSafe(K key) {
        return containsKey(key) ? Optional.of(get(key)) : Optional.empty();
    }

    @NotNull
    public V getOrDefault(K key, Supplier<V> orElse) {
        if (!containsKey(key)) {
            return orElse.get();
        }

        return get(key);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
