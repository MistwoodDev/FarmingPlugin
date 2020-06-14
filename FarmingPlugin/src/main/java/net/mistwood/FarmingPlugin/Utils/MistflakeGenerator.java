package net.mistwood.FarmingPlugin.Utils;

import java.time.Instant;

public class MistflakeGenerator {

    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;

    private static final int maxNodeId = (int)(Math.pow(2, NODE_ID_BITS) - 1);
    private static final int maxSequence = (int)(Math.pow(2, SEQUENCE_BITS) - 1);

    private static final long CUSTOM_EPOCH = 1420070400000L;

    private final int nodeId;

    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    public MistflakeGenerator(int nodeId) {
        if (nodeId < 0 || nodeId > maxNodeId) {
            throw new IllegalArgumentException(String.format("NodeId must be between %d and %d", 0, maxNodeId));
        }

        this.nodeId = nodeId;
    }

    public synchronized long nextId() {
        long currentTimestamp = timestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Invalid System Clock!");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        long id = currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS);
        id |= (nodeId << SEQUENCE_BITS);
        id |= sequence;
        return id;
    }

    private static long timestamp() {
        return Instant.now().toEpochMilli() - CUSTOM_EPOCH;
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp();
        }

        return currentTimestamp;
    }

}
