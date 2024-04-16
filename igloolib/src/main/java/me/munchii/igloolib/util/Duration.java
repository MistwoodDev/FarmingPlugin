package me.munchii.igloolib.util;

public record Duration(int time, TimeUnit timeUnit) {
    public long getTicks() {
        return timeUnit.convertToTicks(time);
    }

    public Duration add(Duration duration) {
        if (duration.timeUnit != timeUnit) {
            throw new IllegalArgumentException("Cannot add two durations with different time units");
        }

        return new Duration(time + duration.time, timeUnit);
    }

    public Duration add(int time) {
        return new Duration(this.time + time, timeUnit);
    }
}
