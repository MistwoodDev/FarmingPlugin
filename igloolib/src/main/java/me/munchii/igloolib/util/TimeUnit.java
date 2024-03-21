package me.munchii.igloolib.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public enum TimeUnit {
    TICK(Long::valueOf),
    MILLISECOND((value) -> value * 20L / 100),
    SECOND((value) -> value * 20L),
    MINUTE((value) -> value * (60L * 20L)),
    HOUR((value) -> value * (60L * 60L * 20L)),
    DAY((value) -> value * (24L * 60L * 60L * 20L));

    private final Function<Integer, Long> ticksConverter;

    TimeUnit(Function<Integer, Long> ticksConverter) {
        this.ticksConverter = ticksConverter;
    }

    public long convertToTicks(int value) {
        return ticksConverter.apply(value);
    }

    public static long make(List<Pair<Integer, TimeUnit>> times) {
        AtomicLong ticks = new AtomicLong();
        times.forEach(time -> ticks.addAndGet(time.getSecond().convertToTicks(time.getFirst())));

        return ticks.get();
    }

    public static long make(Pair<Integer, TimeUnit>... times) {
        AtomicLong ticks = new AtomicLong();
        Arrays.stream(times).forEach(time -> ticks.addAndGet(time.getSecond().convertToTicks(time.getFirst())));

        return ticks.get();
    }
}
