package net.mistwood.FarmingPlugin;

public abstract class Module {

    private final String name;
    private final String version;
    private final String mainCommand;

    public Module(String name, String version) {
        this(name, version, "");
    }

    public Module(String name, String version, String mainCommand) {
        this.name = name;
        this.version = version;
        this.mainCommand = mainCommand;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMainCommand() {
        return mainCommand;
    }

}
