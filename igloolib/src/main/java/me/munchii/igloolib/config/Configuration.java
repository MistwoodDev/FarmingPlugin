package me.munchii.igloolib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    // TODO use toml instead?

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Class clazz;
    private final JavaPlugin plugin;

    private static final String EXTENSION = ".json";

    public Configuration(Class clazz, JavaPlugin plugin) {
        this.clazz = clazz;
        this.plugin = plugin;

        setup();
    }

    private void setup() {
        final File dataDir = plugin.getDataFolder();

        if (!dataDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dataDir.mkdirs();
        }

        final File[] configFiles = dataDir.listFiles();
        if (configFiles != null) {
            final HashMap<String, JsonObject> configs = new HashMap<>();
            for (File file : configFiles) {
                if (!file.getName().endsWith(EXTENSION)) {
                    continue;
                }

                final String name = file.getName().substring(0, file.getName().length() - (EXTENSION.length()));

                try {
                    final String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    final JsonObject jsonObject = GSON.fromJson(fileContents, JsonObject.class);
                    configs.put(name, jsonObject);
                } catch (IOException e) {
                    plugin.getLogger().warning("failed to read config file " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }

            loadFromJson(configs);
        }

        for (Map.Entry<String, JsonObject> entry : toJson().entrySet()) {
            final File configFile = new File(dataDir, entry.getKey() + EXTENSION);
            final String jsonStr = GSON.toJson(entry.getValue());

            try {
                FileUtils.writeStringToFile(configFile, jsonStr, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("failed to write config file " + configFile.getAbsolutePath(), e);
            }
        }
    }

    private HashMap<Field, Config> getConfigFields() {
        final HashMap<Field, Config> fields = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Config.class)) {
                continue;
            }

            if (!Modifier.isStatic(field.getModifiers())) {
                throw new UnsupportedOperationException("config fields must be static");
            }

            Config config = field.getAnnotation(Config.class);
            fields.put(field, config);
        }

        return fields;
    }

    public HashMap<String, JsonObject> toJson() {
        final HashMap<Field, Config> fields = getConfigFields();
        final HashMap<String, JsonObject> configs = new HashMap<>();

        for (Map.Entry<Field, Config> entry : fields.entrySet()) {
            Field field = entry.getKey();
            Config annotation = entry.getValue();

            final JsonObject config = configs.computeIfAbsent(annotation.config(), s -> new JsonObject());

            JsonObject categoryObject;
            if (config.has(annotation.category())) {
                categoryObject = config.getAsJsonObject(annotation.category());
            } else {
                categoryObject = new JsonObject();
                config.add(annotation.category(), categoryObject);
            }

            String key = annotation.key().isEmpty() ? field.getName() : annotation.key();
            if (categoryObject.has(key)) {
                throw new UnsupportedOperationException("duplicate config key found " + key);
            }

            JsonObject fieldObject = new JsonObject();
            fieldObject.addProperty("comment", annotation.comment());

            Object value;
            try {
                value = field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            JsonElement jsonElement = GSON.toJsonTree(value);
            fieldObject.add("value", jsonElement);

            categoryObject.add(key, fieldObject);
        }

        return configs;
    }

    public void loadFromJson(HashMap<String, JsonObject> configs) {
        final HashMap<Field, Config> fields = getConfigFields();

        for (Map.Entry<Field, Config> entry : fields.entrySet()) {
            Field field = entry.getKey();
            Config annotation = entry.getValue();

            final JsonObject config = configs.get(annotation.config());

            if (config == null) {
                continue;
            }

            JsonObject categoryObject = config.getAsJsonObject(annotation.category());
            if (categoryObject == null) {
                continue;
            }

            String key = annotation.key().isEmpty() ? field.getName() : annotation.key();
            if (!categoryObject.has(key)) {
                continue;
            }

            JsonObject fieldObject = categoryObject.get(key).getAsJsonObject();
            if (!fieldObject.has("value")) {
                continue;
            }

            JsonElement jsonValue = fieldObject.get("value");
            Class<?> fieldType = field.getType();

            Object fieldValue = GSON.fromJson(jsonValue, fieldType);

            try {
                field.set(null, fieldValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("failed to set value for config field " + field.getName(), e);
            }
        }
    }
}
