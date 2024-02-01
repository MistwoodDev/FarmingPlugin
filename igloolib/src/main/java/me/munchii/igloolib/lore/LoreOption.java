package me.munchii.igloolib.lore;

import java.util.Optional;

public class LoreOption<T> {
    private final String key;
    private T value = null;

    public LoreOption(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Optional<T> getValue() {
        return isNull() ? Optional.empty() : Optional.of(value);
    }

    protected void setValue(Object value) {
        //noinspection unchecked
        this.value = (T) value;
    }

    public boolean isNull() {
        return value == null;
    }
}
