package me.munchii.igloolib.position;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class ImmutablePosition implements Position {
    @NotNull
    private final String worldName;

    private final double x;
    private final double y;
    private final double z;

    public ImmutablePosition(@NotNull String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ImmutablePosition(@NotNull World world, double x, double y, double z) {
        this(world.getName(), x, y, z);
    }

    public static ImmutablePosition of(@NotNull Location loc) {
        assert loc.getWorld() != null;
        return new ImmutablePosition(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
    }

    public static ImmutablePosition of(@NotNull Position position) {
        if (position instanceof ImmutablePosition) {
            return (ImmutablePosition) position;
        } else {
            return new ImmutablePosition(position.getWorldName(), position.getX(), position.getY(), position.getZ());
        }
    }

    @Override
    public @NotNull String getWorldName() {
        return worldName;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public @Nullable World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    public boolean isInSameWorld(@NotNull Position pos) {
        return isInWorld(pos.getWorldName());
    }

    @Override
    public boolean isInSameWorld(@NotNull Location loc) {
        if (loc.getWorld() == null) return false;
        return isInWorld(loc.getWorld());
    }

    @Override
    public boolean isInSameWorld(@NotNull Entity entity) {
        return isInWorld(entity.getWorld());
    }

    @Override
    public boolean isInWorld(@NotNull World world) {
        return world != null && isInWorld(world.getName());
    }

    @Override
    public boolean isInWorld(@NotNull String worldName) {
        return worldName != null && worldName.toLowerCase(Locale.ROOT).equals(this.worldName.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean isInBox(@NotNull BoundingBox box) {
        return box.contains(x, y, z);
    }

    @Override
    public int getBlockX() {
        return Location.locToBlock(x);
    }

    @Override
    public int getBlockY() {
        return Location.locToBlock(y);
    }

    @Override
    public int getBlockZ() {
        return Location.locToBlock(z);
    }

    @Override
    public @NotNull Position add(double x, double y, double z) {
        return new ImmutablePosition(this.worldName, this.x + x, this.y + y, this.z + z);
    }

    @Override
    public @NotNull Position add(@NotNull Position pos) {
        return new ImmutablePosition(this.worldName, this.x + pos.getX(), this.y + pos.getY(), this.z + pos.getZ());
    }

    @Override
    public @NotNull Position add(Location loc) {
        return new ImmutablePosition(this.worldName, this.x + loc.getX(), this.y + loc.getY(), this.z + loc.getZ());
    }

    @Override
    public @NotNull Position add(Vector vector) {
        return new ImmutablePosition(this.worldName, this.x + vector.getX(), this.y + vector.getY(), this.z + vector.getZ());
    }

    @Override
    public @NotNull Position subtract(double x, double y, double z) {
        return new ImmutablePosition(this.worldName, this.x - x, this.y - y, this.z - z);
    }

    @Override
    public @NotNull Position subtract(@NotNull Position pos) {
        return new ImmutablePosition(this.worldName, this.x - pos.getX(), this.y - pos.getY(), this.z - pos.getZ());
    }

    @Override
    public @NotNull Position subtract(Location loc) {
        return new ImmutablePosition(this.worldName, this.x - loc.getX(), this.y - loc.getY(), this.z - loc.getZ());
    }

    @Override
    public @NotNull Position subtract(Vector vector) {
        return new ImmutablePosition(this.worldName, this.x - vector.getX(), this.y - vector.getY(), this.z - vector.getZ());
    }

    @Override
    public double distance(@NotNull Position pos) {
        return Math.sqrt(distanceSquared(pos));
    }

    @Override
    public double distance(@NotNull Location loc) {
        return Math.sqrt(distanceSquared(loc));
    }

    @Override
    public double distance(@NotNull Entity entity) {
        return Math.sqrt(distanceSquared(entity));
    }

    @Override
    public double distanceSquared(@NotNull Position pos) {
        if (!isInSameWorld(pos)) return -1;

        return NumberConversions.square(this.x - pos.getX())
                + NumberConversions.square(this.y - pos.getY())
                + NumberConversions.square(this.z - pos.getZ());
    }

    @Override
    public double distanceSquared(@NotNull Location loc) {
        if (!isInSameWorld(loc)) return -1;

        return NumberConversions.square(this.x - loc.getX())
                + NumberConversions.square(this.y - loc.getY())
                + NumberConversions.square(this.z - loc.getZ());
    }

    @Override
    public double distanceSquared(@NotNull Entity entity) {
        return distanceSquared(entity.getLocation());
    }

    @Override
    public @Nullable Vector getChunkRelativePosition() {
        return new Vector(getBlockX() & 0xF, getBlockY() & 0xFF, getBlockZ() & 0xF);
    }

    @Override
    public @NotNull Location toLocation() {
        return new Location(getWorld(), this.x, this.y, this.z);
    }

    @Override
    public @NotNull Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ImmutablePosition other = (ImmutablePosition) obj;
        return this.worldName.equals(other.worldName)
                && Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x)
                && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y)
                && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + worldName.hashCode();
        result = 31 * result + Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        result = 31 * result + Double.hashCode(z);
        return result;
    }

    @Override
    public String toString() {
        return "Position{"
                + "worldName=" + worldName
                + ", x=" + x
                + ", y=" + y
                + ", z=" + z
                + "}";
    }
}
