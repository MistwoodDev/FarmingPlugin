package me.munchii.igloolib.module;

import me.munchii.igloolib.Igloolib;
import me.munchii.igloolib.command.CommandManager;
import me.munchii.igloolib.util.ListenerManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class PluginModule {
    private final String name;
    private final List<Listener> listeners;
    private boolean enabled;

    private final JavaPlugin instance;
    private boolean initialized;

    private final CommandManager commandManager;
    private final ListenerManager listenerManager;

    public PluginModule(String name, boolean enabled) {
        this.name = name;
        this.listeners = new ArrayList<>();
        this.enabled = enabled;

        this.instance = Igloolib.INSTANCE;
        this.initialized = false;

        this.commandManager = new CommandManager();
        this.listenerManager = new ListenerManager();
    }

    public abstract void onEnable();
    public abstract void onDisable();

    protected void enable() {
        enabled = true;
        initialized = true;

        listenerManager.enableAll();

        commandManager.enable();

        this.onEnable();
    }

    protected void disable() {
        enabled = false;
        initialized = false;

        listenerManager.disableAll();

        commandManager.disable();

        this.onDisable();
    }

    public void registerListener(Supplier<Listener> listener) {
        Listener l = listenerManager.put(listener, false);
        if (initialized) {
            listenerManager.enable(l.getClass());
        }
    }

    public void deregisterListener(Listener listener) {
        listenerManager.disable(listener.getClass());
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
