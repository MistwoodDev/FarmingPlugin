package me.munchii.igloolib.util;

import java.util.UUID;

public class UUIDCache<T> extends SizedCache<UUID, T> {
    public UUIDCache(int maxSize) {
        super(maxSize);
    }
}
