package me.munchii.igloolib.util;

import com.google.common.base.Joiner;
import me.munchii.igloolib.Igloolib;

import java.util.function.Supplier;

public class Logger {
    public static <T> void info(T value) {
        Igloolib.INSTANCE.getLogger().info(String.valueOf(value));
    }

    @SafeVarargs
    public static <T> void info(T... value) {
        Igloolib.INSTANCE.getLogger().info(Joiner.on(' ').join(value));
    }

    public static <T> void info(Supplier<T> value) {
        Igloolib.INSTANCE.getLogger().info(String.valueOf(value.get()));
    }

    public static <T> void fine(T value) {
        Igloolib.INSTANCE.getLogger().fine(String.valueOf(value));
    }

    @SafeVarargs
    public static <T> void fine(T... value) {
        Igloolib.INSTANCE.getLogger().fine(Joiner.on(' ').join(value));
    }

    public static <T> void fine(Supplier<T> value) {
        Igloolib.INSTANCE.getLogger().fine(String.valueOf(value.get()));
    }

    public static <T> void finer(T value) {
        Igloolib.INSTANCE.getLogger().finer(String.valueOf(value));
    }

    @SafeVarargs
    public static <T> void finer(T... value) {
        Igloolib.INSTANCE.getLogger().finer(Joiner.on(' ').join(value));
    }

    public static <T> void finer(Supplier<T> value) {
        Igloolib.INSTANCE.getLogger().finer(String.valueOf(value.get()));
    }

    public static <T> void finest(T value) {
        Igloolib.INSTANCE.getLogger().finest(String.valueOf(value));
    }

    @SafeVarargs
    public static <T> void finest(T... value) {
        Igloolib.INSTANCE.getLogger().finest(Joiner.on(' ').join(value));
    }

    public static <T> void finest(Supplier<T> value) {
        Igloolib.INSTANCE.getLogger().finest(String.valueOf(value.get()));
    }

    public static <T> void warning(T value) {
        Igloolib.INSTANCE.getLogger().warning(String.valueOf(value));
    }

    @SafeVarargs
    public static <T> void warning(T... value) {
        Igloolib.INSTANCE.getLogger().warning(Joiner.on(' ').join(value));
    }

    public static <T> void warning(Supplier<T> value) {
        Igloolib.INSTANCE.getLogger().warning(String.valueOf(value.get()));
    }

    public static <T> void severe(T value) {
        Igloolib.INSTANCE.getLogger().severe(String.valueOf(value));
    }

    @SafeVarargs
    public static <T> void severe(T... value) {
        Igloolib.INSTANCE.getLogger().severe(Joiner.on(' ').join(value));
    }

    public static <T> void severe(Supplier<T> value) {
        Igloolib.INSTANCE.getLogger().severe(String.valueOf(value.get()));
    }
}
