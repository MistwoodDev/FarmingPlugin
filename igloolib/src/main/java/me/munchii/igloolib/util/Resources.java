package me.munchii.igloolib.util;

import me.munchii.igloolib.Igloolib;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Resources {
    public static Stream<String> readResourceFile(JavaPlugin plugin, String path) {
        try {
            File file = new File(plugin.getDataFolder(), path);
            InputStream stream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(stream);
            return new BufferedReader(streamReader).lines();
        } catch (FileNotFoundException e) {
            Logger.warning(e::getMessage);
            return null;
        }
    }

    public static void readResourceFile(JavaPlugin plugin, String path, Consumer<String> consumer) {
        try {
            File file = new File(plugin.getDataFolder(), path);
            InputStream stream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(streamReader);
            reader.lines().forEach(consumer);
        } catch (FileNotFoundException e) {
            Logger.warning(e::getMessage);
        }
    }

    public static void readProjectResourceFile(String path, Consumer<String> consumer) {
        try {
            File file = new File("src/main/resources", path);
            InputStream stream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(streamReader);
            reader.lines().forEach(consumer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Optional<File> getDataFile(JavaPlugin plugin, String path) {
        Path filePath = Path.of(plugin.getDataFolder().getPath(), "data", path, ".json");
        File file = new File(plugin.getDataFolder() + "/data", path + ".json");
        if (!file.exists()) {
            return createDataFile(plugin, path);
        } else {
            return Optional.of(file);
        }
    }

    public static Optional<File> createDataFile(JavaPlugin plugin, String path) {
        Path filePath = Path.of(plugin.getDataFolder().getPath(), "data", path, ".json");
        File file = new File(filePath.toUri());
        try {
            if (file.createNewFile()) {
                return Optional.of(file);
            }
        } catch (IOException e) {
            Logger.severe("could not create data file: " + filePath.toUri());
        }

        return Optional.empty();
    }

    public static NamespacedKey id(JavaPlugin plugin, String path) {
        return new NamespacedKey(plugin, path);
    }

    public static NamespacedKey id(String path) {
        return id(Igloolib.INSTANCE, path);
    }
}
