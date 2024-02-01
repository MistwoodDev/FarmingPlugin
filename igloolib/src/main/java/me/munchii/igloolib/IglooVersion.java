package me.munchii.igloolib;

import java.util.function.Supplier;

public class IglooVersion {
    // no idea if this actually works. it works in dev/test env
    public static final Supplier<Environment> ENV = () -> {
        try {
            Class<?> pluginClass = Class.forName("org.bukkit.plugin.java.JavaPlugin");
            return Environment.RELEASE;
        } catch (ClassNotFoundException e) {
            return Environment.DEV;
        }
    };

    public enum Environment {
        RELEASE(false),
        DEV(true);

        private final boolean development;

        Environment(boolean isDev) {
            development = isDev;
        }

        public final boolean isDevelopment() {
            return development;
        }

        public final boolean isRelease() {
            return !development;
        }
    }
}
