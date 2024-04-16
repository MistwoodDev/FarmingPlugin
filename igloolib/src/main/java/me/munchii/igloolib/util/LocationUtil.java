package me.munchii.igloolib.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class LocationUtil {
    @NotNull
    public static Set<Location> getLocationsInChunk(@NotNull Chunk chunk) {
        Set<Location> locations = new HashSet<>();

        for (int x = 0; x <= 15; ++x) {
            for (int y = 0; y <= chunk.getWorld().getMaxHeight(); ++y) {
                for (int z = 0; z <= 15; ++z) {
                    locations.add(chunk.getBlock(x, y, z).getLocation());
                }
            }
        }

        return locations;
    }

    @NotNull
    public static String toSimpleString(@NotNull Location location) {
        return "{x=" + location.getBlockX() + ", y=" + location.getBlockY() + ", z=" + location.getBlockZ() + "}";
    }
}
