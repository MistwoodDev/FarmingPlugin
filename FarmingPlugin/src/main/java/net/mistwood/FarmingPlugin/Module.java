package net.mistwood.FarmingPlugin;

public abstract class Module
{

    private final String Name;
    private final String Version;
    private final String MainCommand;

    public Module (String Name, String Version)
    {
        this (Name, Version, "");
    }

    public Module (String Name, String Version, String MainCommand)
    {
        this.Name = Name;
        this.Version = Version;
        this.MainCommand = MainCommand;
    }

    public abstract void OnEnable (Main Instance);
    public abstract void OnDisable ();

    public String GetName ()
    {
        return Name;
    }

    public String GetVersion ()
    {
        return Version;
    }

    public String GetMainCommand ()
    {
        return MainCommand;
    }

}
