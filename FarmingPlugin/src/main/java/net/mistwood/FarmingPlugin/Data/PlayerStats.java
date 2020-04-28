package net.mistwood.FarmingPlugin.Data;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats implements Data
{

    private PlayerStats ()
    {
        // TODO
    }

    public static PlayerStats Init ()
    {
        return new PlayerStats ();
    }

    @Override
    public Map<String, Object> ToMap ()
    {
        Map<String, Object> Data = new HashMap<String, Object>();

        return Data;
    }

    public static PlayerStats FromMap (Map<String, Object> Data)
    {
        return new PlayerStats ();
    }

}
