package me.munchii.igloolib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DynamicDataFileContainer {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<String, JsonElement> data;
    private final File file;
    private boolean initialized;

    public DynamicDataFileContainer(File file) {
        this.file = file;
        this.data = loadData();
    }

    private Map<String, JsonElement> loadData() {
        if (!file.exists()) {
            initialized = false;
            return new HashMap<>();
        }

        try {
            final String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            final JsonObject jsonObject = GSON.fromJson(fileContents, JsonObject.class);
            initialized = true;
            return jsonObject.asMap();
        } catch (IOException e) {
            initialized = false;
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    public Optional<Boolean> getBoolean(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsBoolean());
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

    public Optional<String> getString(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsString());
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

    public Optional<BigInteger> getBigInteger(String key) {
        if (isInitialized()) {
            if (contains(key)) {
                return Optional.of(data.get(key).getAsBigInteger());
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



    public boolean contains(String key) {
        return data.containsKey(key);
    }

    public boolean isInitialized() {
        return initialized;
    }
}
