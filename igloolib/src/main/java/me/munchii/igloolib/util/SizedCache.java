package me.munchii.igloolib.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SizedCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public SizedCache(int maxSize) {
        super(maxSize + 2, 1F);

        this.maxSize = maxSize;
    }

    public Optional<V> getSafe(K key) {
        return containsKey(key) ? Optional.of(get(key)) : Optional.empty();
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
