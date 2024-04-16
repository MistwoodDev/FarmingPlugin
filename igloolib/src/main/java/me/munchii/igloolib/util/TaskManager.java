package me.munchii.igloolib.util;

import me.munchii.igloolib.Igloolib;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public enum TaskManager {
    INSTANCE;

    private final Map<String, Runnable> tasks;
    private final Map<Integer, String> taskIds;

    TaskManager() {
        this.tasks = new HashMap<>();
        this.taskIds = new HashMap<>();
    }

    public static void registerTask(String name, Runnable task) {
        INSTANCE.tasks.putIfAbsent(name, task);
    }

    public static BukkitTask registerRepeatingTask(String name, Runnable task, int delay, int period, TimeUnit timeUnit) {
        registerTask(name, task);

        long delayTicks = timeUnit.convertToTicks(delay);
        long periodTicks = timeUnit.convertToTicks(period);
        BukkitTask bukkitTask = Bukkit.getServer().getScheduler().runTaskTimer(Igloolib.INSTANCE, task, delayTicks, periodTicks);
        INSTANCE.taskIds.put(bukkitTask.getTaskId(), name);
        return bukkitTask;
    }

    public static BukkitTask registerRepeatingAsyncTask(String name, Runnable task, int delay, int period, TimeUnit timeUnit) {
        registerTask(name, task);

        long delayTicks = timeUnit.convertToTicks(delay);
        long periodTicks = timeUnit.convertToTicks(period);
        BukkitTask bukkitTask = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Igloolib.INSTANCE, task, delayTicks, periodTicks);
        INSTANCE.taskIds.put(bukkitTask.getTaskId(), name);
        return bukkitTask;
    }

    public static Optional<Runnable> getTask(String name) {
        return INSTANCE.tasks.containsKey(name) ? Optional.of(INSTANCE.tasks.get(name)) : Optional.empty();
    }

    public static Optional<Integer> getTaskId(String name) {
        for (Map.Entry<Integer, String> entry: INSTANCE.taskIds.entrySet()) {
            if (entry.getValue().equals(name)) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
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

    public static void runAnonymousDelayedTask(Runnable runnable, int delay, TimeUnit timeUnit) {
        Bukkit.getServer().getScheduler().runTaskLater(Igloolib.INSTANCE, runnable, timeUnit.convertToTicks(delay));
    }

    public static void runAnonymousDelayedAsyncTask(Runnable runnable, int delay, TimeUnit timeUnit) {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Igloolib.INSTANCE, runnable, timeUnit.convertToTicks(delay));
    }

    public static void removeTask(String name) {
        INSTANCE.tasks.remove(name);
        getTaskId(name).ifPresent(id -> Bukkit.getServer().getScheduler().cancelTask(id));
    }

    public static Set<String> getKeys() {
        return INSTANCE.tasks.keySet();
    }
}
