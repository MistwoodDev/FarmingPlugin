package me.munchii.igloolib.module;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModuleManager {
    private final Map<String, PluginModule> modules;

    public ModuleManager() {
        this.modules = new HashMap<>();
    }

    public void registerModule(PluginModule module) {
        this.modules.put(module.getName(), module);

        if (module.isEnabled()) {
            module.enable();
        }
    }

    public void registerModule(Supplier<PluginModule> module) {
        registerModule(module.get());
    }

    public void setModuleEnabled(String name, boolean enabled) {
        PluginModule module = getModule(name);

        if (enabled) {
            if (module.isEnabled()) {
                module.enable();
            }
        } else {
            if (!module.isEnabled()) {
                module.disable();
            }
        }
    }

    public void enableModule(String name) {
        setModuleEnabled(name, true);
    }

    public void disableModule(String name) {
        setModuleEnabled(name, false);
    }

    public PluginModule getModule(String name) {
        return modules.get(name);
    }
}
