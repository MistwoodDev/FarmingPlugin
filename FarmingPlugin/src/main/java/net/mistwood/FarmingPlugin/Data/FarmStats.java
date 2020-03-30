package net.mistwood.FarmingPlugin.Data;

import java.util.HashMap;
import java.util.Map;

public class FarmStats
{

    public int Crops;

    public static FarmStats Init ()
    {
        return new FarmStats (
                0
        );
    }

    private FarmStats (int Crops)
    {
        this.Crops = Crops;
    }

    public Map<String, Object> ToMap ()
    {
        Map<String, Object> Data = new HashMap<String, Object> ();
        Data.put ("Crops", Crops);

        return Data;
    }

    public static FarmStats FromMap (Map<String, Object> Data)
    {
        return new FarmStats (
                0
        );
    }

}
