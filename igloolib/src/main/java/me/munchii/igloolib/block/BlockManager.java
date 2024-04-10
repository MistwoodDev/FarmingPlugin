package me.munchii.igloolib.block;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public enum BlockManager {
    INSTANCE;

    private final Map<Location, IglooBlock> blocks = new HashMap<>();
}
