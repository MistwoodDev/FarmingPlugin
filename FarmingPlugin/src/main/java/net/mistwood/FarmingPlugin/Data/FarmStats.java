package net.mistwood.FarmingPlugin.Data;

import java.util.HashMap;
import java.util.Map;

public class FarmStats implements Data {

    public int crops;

    private FarmStats(int crops) {
        this.crops = crops;
    }

    public static FarmStats init() {
        return new FarmStats(
                0
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("Crops", crops);

        return data;
    }

    public static FarmStats fromMap(Map<String, Object> data) {
        return new FarmStats(
                (Integer) data.get("Crops")
        );
    }

}
