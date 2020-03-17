package net.mistwood.FarmingPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Cache<T>
{

    private HashMap<UUID, T> CachedItems = new HashMap<UUID, T> ();

    public T Get (UUID ID)
    {
        return CachedItems.get (ID);
    }

    public void Add (UUID ID, T Value)
    {
        CachedItems.put (ID, Value);
    }

    // NOTE: This is just the same as the add method, but I still wanted it there
    public void Update (UUID ID, T Value)
    {
        CachedItems.put (ID, Value);
    }

    public void Remove (UUID ID)
    {
        CachedItems.remove (ID);
    }

    public void Remove (T Value)
    {
        for (UUID ID : CachedItems.keySet ())
        {
            if (CachedItems.get (ID) == Value)
                CachedItems.remove (ID);
        }
    }

}
