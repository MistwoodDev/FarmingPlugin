package me.munchii.igloolib.position;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Position {
    @NotNull
    String getWorldName();

    double getX();

    double getY();

    double getZ();

    @Nullable
    World getWorld();

    boolean isInSameWorld(@NotNull Position pos);

    boolean isInSameWorld(@NotNull Location loc);

    boolean isInSameWorld(@NotNull Entity entity);

    boolean isInWorld(@NotNull World world);

    boolean isInWorld(@NotNull String worldName);

    boolean isInBox(@NotNull BoundingBox box);

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    @NotNull
    Position add(double x, double y, double z);

    @NotNull
    Position add(@NotNull Position pos);

    @NotNull
    Position add(Location loc);

    @NotNull
    Position add(Vector vector);

    @NotNull
    Position subtract(double x, double y, double z);

    @NotNull
    Position subtract(@NotNull Position pos);

    @NotNull
    Position subtract(Location loc);

    @NotNull
    Position subtract(Vector vector);

    double distance(@NotNull Position pos);

    double distance(@NotNull Location loc);

    double distance(@NotNull Entity entity);

    double distanceSquared(@NotNull Position pos);

    double distanceSquared(@NotNull Location loc);

    double distanceSquared(@NotNull Entity entity);

    @Nullable
    Vector getChunkRelativePosition();

    @NotNull
    Location toLocation();

    @NotNull
    Vector toVector();
}
