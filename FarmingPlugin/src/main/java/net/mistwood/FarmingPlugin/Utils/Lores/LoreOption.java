package net.mistwood.FarmingPlugin.Utils.Lores;

public class LoreOption<T> {

    public final String key;
    private T value = null;

    public LoreOption(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = (T) value;
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean notNull() {
        return !isNull();
    }

    public String toString() {
        return isNull() ? null : value.toString();
    }

}
