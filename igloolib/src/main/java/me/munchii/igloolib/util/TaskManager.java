package me.munchii.igloolib.util;

import me.munchii.igloolib.Igloolib;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public enum TaskManager {
    INSTANCE;

    private final Map<String, Runnable> tasks;

    TaskManager() {
        this.tasks = new HashMap<>();
    }

    public static void registerTask(String name, Runnable task) {
        INSTANCE.tasks.putIfAbsent(name, task);
    }

    public static void registerRepeatingTask(String name, Runnable task, int delay, int period, TimeUnit timeUnit) {
        registerTask(name, task);

        long delayTicks = timeUnit.convertToTicks(delay);
        long periodTicks = timeUnit.convertToTicks(period);
        Bukkit.getServer().getScheduler().runTaskTimer(Igloolib.INSTANCE, task, delayTicks, periodTicks);
    }

    public static void registerRepeatingAsyncTask(String name, Runnable task, int delay, int period, TimeUnit timeUnit) {
        registerTask(name, task);

        long delayTicks = timeUnit.convertToTicks(delay);
        long periodTicks = timeUnit.convertToTicks(period);
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Igloolib.INSTANCE, task, delayTicks, periodTicks);
    }

    public static Optional<Runnable> getTask(String name) {
        return INSTANCE.tasks.containsKey(name) ? Optional.of(INSTANCE.tasks.get(name)) : Optional.empty();
    }

    public static void runTask(String name) {
        getTask(name).ifPresent(task -> Bukkit.getServer().getScheduler().runTask(Igloolib.INSTANCE, task));
    }

    public static void runAsyncTask(String name) {
        getTask(name).ifPresent(task -> Bukkit.getServer().getScheduler().runTaskAsynchronously(Igloolib.INSTANCE, task));
    }

    public static void runAnonymousTask(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTask(Igloolib.INSTANCE, runnable);
    }

    public static void runAnonymousAsyncTask(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Igloolib.INSTANCE, runnable);
    }

    public static Set<String> getKeys() {
        return INSTANCE.tasks.keySet();
    }
}
