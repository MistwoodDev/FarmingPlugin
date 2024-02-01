package me.munchii.igloolib.util;

public class Pair<K, V> {
    private K first;
    private V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public void setFirst(K value) {
        first = value;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V value) {
        second = value;
    }
}
