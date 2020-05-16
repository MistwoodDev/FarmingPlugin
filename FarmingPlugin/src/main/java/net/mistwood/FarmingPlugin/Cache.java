package net.mistwood.FarmingPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Cache<T> {

    private final HashMap<UUID, T> cachedItems = new HashMap<>();

    public T get(UUID ID) {
        return cachedItems.get(ID);
    }

    public void add(UUID ID, T Value) {
        cachedItems.put(ID, Value);
    }

    public void update(UUID ID, T Value) {
        cachedItems.put(ID, Value);
    }

    public void remove(UUID ID) {
        cachedItems.remove(ID);
    }

    public void remove(T Value) {
        for (UUID id : cachedItems.keySet()) {
            if (cachedItems.get(id) == Value)
                cachedItems.remove(id);
        }
    }

}
