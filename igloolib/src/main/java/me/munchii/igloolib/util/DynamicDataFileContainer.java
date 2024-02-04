package me.munchii.igloolib.util;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class DynamicDataFileContainer {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final JsonObject data;

    private final JavaPlugin plugin;
    private final File file;
    private final String taskName;
    private boolean initialized;

    public DynamicDataFileContainer(JavaPlugin plugin, String filename) {
        this.plugin = plugin;
        this.file = Resources.getDataFile(plugin, filename).orElseThrow();

        this.data = loadData();

        this.taskName = "DynamicDataFileContainer_save_" + plugin.getName() + "_" + "filename";
        TaskManager.registerRepeatingAsyncTask(taskName, () -> {
            try {
                GSON.toJson(data, new FileWriter(file));
            } catch (IOException e) {
                Logger.warning("could not save dynamic data file:", file.getPath());
            }
        }, 15, 15, TimeUnit.MINUTE);
    }

    private JsonObject loadData() {
        if (!file.exists()) {
            initialized = false;
            return new JsonObject();
        }

        try {
            final String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            final JsonObject jsonObject = GSON.fromJson(fileContents, JsonObject.class);
            initialized = true;
            return jsonObject;
        } catch (IOException e) {
            initialized = false;
            Logger.severe(e.getMessage());
        }

        return new JsonObject();
    }

    public Optional<JsonObject> getJsonObject(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsJsonObject());
            }
        }

        return Optional.empty();
    }

    public Optional<JsonArray> getJsonArray(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsJsonArray());
            }
        }

        return Optional.empty();
    }

    public Optional<JsonNull> getJsonNull(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsJsonNull());
            }
        }

        return Optional.empty();
    }

    public Optional<JsonPrimitive> getJsonPrimitive(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsJsonPrimitive());
            }
        }

        return Optional.empty();
    }

    public Optional<BigInteger> getBigInteger(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsBigInteger());
            }
        }

        return Optional.empty();
    }

    public Optional<BigDecimal> getBigDecimal(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsBigDecimal());
            }
        }

        return Optional.empty();
    }

    public Optional<Boolean> getBoolean(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsBoolean());
            }
        }

        return Optional.empty();
    }

    public Optional<Byte> getByte(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsByte());
            }
        }

        return Optional.empty();
    }

    public Optional<Double> getDouble(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsDouble());
            }
        }

        return Optional.empty();
    }

    public Optional<Float> getFloat(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsFloat());
            }
        }

        return Optional.empty();
    }

    public Optional<Integer> getInt(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsInt());
            }
        }

        return Optional.empty();
    }

    public Optional<Long> getLong(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsLong());
            }
        }

        return Optional.empty();
    }

    public Optional<Number> getNumber(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsNumber());
            }
        }

        return Optional.empty();
    }

    public Optional<Short> getShort(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsShort());
            }
        }

        return Optional.empty();
    }

    public Optional<String> getString(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsString());
            }
        }

        return Optional.empty();
    }

    public void putJsonObject(String key, JsonObject value) {
        if (isInitialized()) {
            if (!contains(key)) {
                data.add(key, value);
            }
        }
    }

    public void putJsonArray(String key, JsonArray value) {
        if (isInitialized()) {
            if (!contains(key)) {
                data.add(key, value);
            }
        }
    }

    public void putJsonNull(String key, JsonNull value) {
        if (isInitialized()) {
            if (!contains(key)) {
                data.add(key, value);
            }
        }
    }

    public void putJsonPrimitive(String key, JsonPrimitive value) {
        if (isInitialized()) {
            if (!contains(key)) {
                data.add(key, value);
            }
        }
    }

    public void putBigInteger(String key, BigInteger value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putBigDecimal(String key, BigDecimal value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putBoolean(String key, Boolean value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putByte(String key, Byte value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putDouble(String key, Double value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putFloat(String key, Float value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putInt(String key, Integer value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putLong(String key, Long value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putNumber(String key, Number value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putShort(String key, Short value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void putString(String key, String value) {
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateJsonObject(String key, JsonObject value) {
        remove(key);
        putJsonObject(key, value);
    }

    public void updateJsonArray(String key, JsonArray value) {
        remove(key);
        putJsonArray(key, value);
    }

    public void updateJsonNull(String key, JsonNull value) {
        remove(key);
        putJsonNull(key, value);
    }

    public void updateJsonPrimitive(String key, JsonPrimitive value) {
        remove(key);
        putJsonPrimitive(key, value);
    }

    public void updateBigInteger(String key, BigInteger value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateBigDecimal(String key, BigDecimal value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateBoolean(String key, Boolean value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateByte(String key, Byte value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateDouble(String key, Double value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateFloat(String key, Float value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateInt(String key, Integer value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateLong(String key, Long value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateNumber(String key, Number value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateShort(String key, Short value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void updateString(String key, String value) {
        remove(key);
        putJsonPrimitive(key, new JsonPrimitive(value));
    }

    public void remove(String key) {
        data.remove(key);
    }

    public boolean contains(String key) {
        return data.has(key);
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void forceSave() {
        TaskManager.runAsyncTask(taskName);
    }
}
