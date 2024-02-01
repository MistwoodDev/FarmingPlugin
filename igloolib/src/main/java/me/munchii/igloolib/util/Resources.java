package me.munchii.igloolib.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Stream;

public class Resources {
    public static Stream<String> readResourceFile(JavaPlugin plugin, String path) {
        try {
            File file = new File(plugin.getDataFolder(), path);
            InputStream stream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(stream);
            return new BufferedReader(streamReader).lines();
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning(e::getMessage);
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
            Bukkit.getLogger().log(Level.SEVERE, e::getMessage);
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
}
