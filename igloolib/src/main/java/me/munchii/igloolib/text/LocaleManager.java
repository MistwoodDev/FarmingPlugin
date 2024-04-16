package me.munchii.igloolib.text;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.munchii.igloolib.IgloolibConfig;
import me.munchii.igloolib.util.Logger;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public enum LocaleManager {
    INSTANCE;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<String, LocaleMap> locales;

    private boolean isInitialized;
    private int loadedFiles = 0;

    LocaleManager() {
        locales = new HashMap<>();
    }

    @Nullable
    public static String get(String locale, String key) {
        if (!INSTANCE.isInitialized) {
            loadPluginLocales();
        }

        if (INSTANCE.locales.containsKey(locale.toLowerCase(Locale.ROOT))) {
            LocaleMap localeMap = INSTANCE.locales.get(locale.toLowerCase(Locale.ROOT));
            if (localeMap.has(key.toLowerCase(Locale.ROOT))) {
                return localeMap.get(key.toLowerCase(Locale.ROOT));
            }
        }

        return null;
    }

    public static void reload() {
        INSTANCE.isInitialized = false;
        INSTANCE.locales.clear();
        loadPluginLocales();
    }

    private static void loadPluginLocales() {
        int localeFiles = 0;
        int failedFiles = 0;
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            final File langDir = Paths.get(plugin.getDataFolder().toURI()).resolve("lang").toFile();
            if (!langDir.exists()) {
                continue;
            }

            for (File file : Objects.requireNonNull(langDir.listFiles())) {
                if (!file.getName().endsWith(".json")) {
                    continue;
                }

                String localeName = file.getName().replace(".json", "").toLowerCase(Locale.ROOT);
                Locale locale = parseLocale(localeName);
                if (!isValidLocale(locale)) {
                    continue;
                }

                LocaleMap localeMap;
                boolean contains;
                if (!INSTANCE.locales.containsKey(localeName)) {
                    localeMap = new LocaleMap();
                    contains = false;
                } else {
                    localeMap = INSTANCE.locales.get(localeName);
                    contains = true;
                }

                try {
                    final String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    final JsonObject jsonObject = GSON.fromJson(fileContents, JsonObject.class);
                    for (Map.Entry<String, JsonElement> entry : jsonObject.asMap().entrySet()) {
                        String key = entry.getKey().toLowerCase(Locale.ROOT);
                        if (localeMap.has(key)) {
                            Logger.warning("LocaleManager: Locale '" + localeName + "' already contains key: " + key);
                            continue;
                        }

                        localeMap.put(key, entry.getValue().getAsString());
                    }
                    localeFiles += 1;
                } catch (IOException e) {
                    failedFiles += 1;
                    Logger.warning("LocaleManager: Failed to read locale file " + file.getAbsolutePath());
                    e.printStackTrace();
                }

                if (contains) {
                    INSTANCE.locales.replace(localeName, localeMap);
                } else {
                    INSTANCE.locales.put(localeName, localeMap);
                }
            }
        }

        INSTANCE.isInitialized = true;
        INSTANCE.loadedFiles = localeFiles;
        if (IgloolibConfig.verbose) Logger.info("LocaleManager: Loaded " + localeFiles + " locale files. Failed to load " + failedFiles + " locale files");
    }

    private static Locale parseLocale(String locale) {
        String[] parts = locale.split("_");
        return switch (parts.length) {
            case 3 -> new Locale(parts[0], parts[1], parts[2]);
            case 2 -> new Locale(parts[0], parts[1]);
            case 1 -> new Locale(parts[0]);
            default -> null;
        };
    }

    private static boolean isValidLocale(Locale locale) {
        try {
            return locale.getISO3Language() != null && locale.getISO3Country() != null;
        } catch (MissingResourceException e) {
            return false;
        }
    }

    public static int getLoadedFiles() {
        return INSTANCE.loadedFiles;
    }

    @ApiStatus.Internal
    public static ImmutableMap<String, LocaleMap> getLocales() {
        return ImmutableMap.copyOf(INSTANCE.locales);
    }

    public static Set<String> keySet() {
        return ImmutableSet.copyOf(INSTANCE.locales.keySet());
    }

    public static class LocaleMap {
        private final Map<String, String> fields;

        public LocaleMap() {
            fields = new HashMap<>();
        }

        public boolean has(String key) {
            return fields.containsKey(key);
        }

        public String get(String key) {
            return fields.get(key);
        }

        public void put(String key, String field) {
            fields.put(key, field);
        }
    }
}
