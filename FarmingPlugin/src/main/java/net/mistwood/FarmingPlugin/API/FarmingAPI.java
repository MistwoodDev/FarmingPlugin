package net.mistwood.FarmingPlugin.API;

import net.mistwood.FarmingPlugin.Data.FarmData;
import net.mistwood.FarmingPlugin.Database.DatabaseCollection;
import net.mistwood.FarmingPlugin.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FarmingAPI
{

    private Main Instance;

    public FarmingAPI (Main Instance)
    {
        this.Instance = Instance;
    }

    public FarmData GetFarm (UUID FarmID)
    {
        List<FarmData> Farm = new ArrayList<FarmData> ();
        Instance.Database.Get (FarmID, DatabaseCollection.FarmsCollection, (Result, Throwable) -> {
            Farm.add (FarmData.FromMap (Result));
        });

        // TODO: Will probably not be set to the farm data, because it's async :(
        return Farm.get (0);
    }

}
