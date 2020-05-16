package net.mistwood.FarmingPlugin.Data;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats implements Data {

    private PlayerStats() {
        // TODO
    }

    public static PlayerStats init() {
        return new PlayerStats();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        return data;
    }

    public static PlayerStats fromMap(Map<String, Object> data) {
        return new PlayerStats();
    }

}
