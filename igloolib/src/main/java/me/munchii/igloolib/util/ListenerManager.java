package me.munchii.igloolib.util;

import me.munchii.igloolib.Igloolib;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ListenerManager {
    private final Map<Class<? extends Listener>, ListenerReference> listeners;

    public ListenerManager() {
        listeners = new HashMap<>();
    }

    public <T extends Listener> T register(Supplier<T> listenerSupplier) {
        T listener = listenerSupplier.get();
        if (isRegistered(listener.getClass())) {
            //noinspection unchecked
            return (T) listeners.get(listener.getClass()).instance;
        }

        listeners.putIfAbsent(listener.getClass(), new ListenerReference(listener));
        enable(listener.getClass());
        return listener;
    }

    public <T extends Listener> T put(Supplier<T> listenerSupplier, boolean enabled) {
        T listener = listenerSupplier.get();
        listeners.putIfAbsent(listener.getClass(), new ListenerReference(listener));
        boolean b = enabled ? enable(listener.getClass()) : disable(listener.getClass());
        return listener;
    }

    public <T extends Listener> boolean isEnabled(Class<T> clazz) {
        if (isRegistered(clazz)) {
            return listeners.get(clazz).enabled;
        }

        return false;
    }

    public <T extends Listener> boolean isRegistered(Class<T> clazz) {
        return listeners.containsKey(clazz);
    }

    public void enableAll() {
        listeners.keySet().forEach(this::enable);
    }

    public <T extends Listener> boolean enable(Class<T> clazz) {
        if (isRegistered(clazz) && !isEnabled(clazz)) {
            ListenerReference ref = listeners.get(clazz);
            if (HandlerList.getRegisteredListeners(Igloolib.INSTANCE).stream().noneMatch(l -> l.getListener().equals(ref.instance))) {
                Bukkit.getPluginManager().registerEvents(ref.instance, Igloolib.INSTANCE);
                ref.enabled = true;
                return true;
            }
        }

        return false;
    }

    public void disableAll() {
        listeners.keySet().forEach(this::disable);
    }

    public <T extends Listener> boolean disable(Class<T> clazz) {
        if (isRegistered(clazz) && isEnabled(clazz)) {
            HandlerList.unregisterAll(listeners.get(clazz).instance);
            listeners.get(clazz).enabled = false;
            return true;
        }

        return false;
    }

    public <T extends Listener> boolean remove(Class<T> clazz) {
        if (!isRegistered(clazz)) {
            return false;
        }

        disable(clazz);
        listeners.remove(clazz);
        return true;
    }

    private static class ListenerReference {
        public final Listener instance;
        public boolean enabled;

        public ListenerReference(Listener instance) {
            this.instance = instance;
            this.enabled = false;
        }
    }
}
