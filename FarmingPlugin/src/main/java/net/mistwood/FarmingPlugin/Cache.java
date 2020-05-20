package net.mistwood.FarmingPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Cache<T> {

    private final HashMap<UUID, T> cachedItems = new HashMap<>();

    public T get(UUID id) {
        return cachedItems.get(id);
    }

    public void add(UUID id, T value) {
        cachedItems.put(id, value);
    }

    public void update(UUID id, T value) {
        cachedItems.put(id, value);
    }

    public void remove(UUID id) {
        cachedItems.remove(id);
    }

    public void remove(T value) {
        for (UUID id : cachedItems.keySet()) {
            if (cachedItems.get(id) == value)
                cachedItems.remove(id);
        }
    }

}
