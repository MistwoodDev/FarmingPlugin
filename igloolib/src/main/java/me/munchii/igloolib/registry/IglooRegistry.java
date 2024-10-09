package me.munchii.igloolib.registry;

import com.google.common.collect.ImmutableSet;
import me.munchii.igloolib.block.IglooBlock;
import me.munchii.igloolib.block.entity.IglooBlockEntityType;
import me.munchii.igloolib.item.IglooItem;
import me.munchii.igloolib.util.Logger;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IglooRegistry<T> extends Iterable<T> {
    IglooRegistry<IglooBlock> BLOCK = new SimpleRegistry<>(IglooBlock.class);
    IglooRegistry<IglooBlockEntityType> BLOCK_ENTITY_TYPE = new SimpleRegistry<>(IglooBlockEntityType.class);
    IglooRegistry<IglooItem> ITEM = new SimpleRegistry<>(IglooItem.class);

    T register(@NotNull NamespacedKey key, @NotNull T value);

    boolean has(@NotNull NamespacedKey key);

    @Nullable
    T get(@NotNull NamespacedKey key);

    default Optional<T> getOrEmpty(@NotNull NamespacedKey key) {
        return Optional.ofNullable(get(key));
    }

    @Nullable
    NamespacedKey getId(T value);

    @NotNull
    Stream<T> stream();

    @NotNull
    Set<NamespacedKey> keySet();

    final class SimpleRegistry<T> implements IglooRegistry<T> {
        private final Class<T> type;
        private final Map<NamespacedKey, T> map;

        private SimpleRegistry(@NotNull Class<T> type) {
            this.type = type;
            this.map = new HashMap<>();
        }

        @Override
        public T register(@NotNull NamespacedKey key, @NotNull T value) {
            if (!has(key)) {
                map.put(key, value);
                return value;
            }

            Logger.severe("IglooRegistry: Attempted to register key '" + key + "' in registry '" + type.getName() + "' which already exists!");
            return map.get(key);
        }

        @Override
        public boolean has(@NotNull NamespacedKey key) {
            return this.map.containsKey(key);
        }

        @Override
        @Nullable
        public T get(@NotNull NamespacedKey key) {
            return this.map.get(key);
        }

        @Override
        @Nullable
        public NamespacedKey getId(T value) {
            return map.entrySet().stream().filter(entry -> entry.getValue().equals(value)).findFirst().map(Map.Entry::getKey).orElse(null);
        }

        @Override
        @NotNull
        public Stream<T> stream() {
            return StreamSupport.stream(this.spliterator(), false);
        }

        @Override
        @NotNull
        public Iterator<T> iterator() {
            return this.map.values().iterator();
        }

        @Override
        @NotNull
        public Set<NamespacedKey> keySet() {
            return ImmutableSet.copyOf(map.keySet());
        }
    }
}
