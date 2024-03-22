package me.munchii.igloolib;

import me.munchii.igloolib.block.BlockEntityManager;
import me.munchii.igloolib.block.DefaultBlockEntityListener;
import me.munchii.igloolib.command.CommandManager;
import me.munchii.igloolib.command.IglooCommand;
import me.munchii.igloolib.command.IglooCommandGroup;
import me.munchii.igloolib.config.Configuration;
import me.munchii.igloolib.gui.DefaultWindowListener;
import me.munchii.igloolib.text.LocaleManager;
import me.munchii.igloolib.util.ListenerManager;
import me.munchii.igloolib.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class Igloolib extends JavaPlugin {

    public static Igloolib INSTANCE = null;
    private static final ListenerManager LISTENERS = new ListenerManager();
    private static CommandManager commandManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        new Configuration(IgloolibConfig.class, this);

        registerListener(DefaultBlockEntityListener::new);
        registerListener(BlockEntityManager.ChunkListener::new);
        registerListener(DefaultWindowListener::new);

        commandManager = new CommandManager();
        // TODO: this command doesn't work. probably something wrong with command groups lol
        commandManager.registerCommandGroup(IglooCommandGroup.create("igloo")
                .withCommand(IglooCommand.create("locale")
                        .withDescription("Locale tools")
                        .withUsage("/locale")
                        .withPermission("igloolib.locale.reload")
                        .withAction(ctx -> {
                            List<String> args = ctx.getArgs();
                            if (Objects.equals(args.get(0), "reload")) {
                                LocaleManager.reload();
                                ctx.getPlayer().sendMessage("&7[igloolib] Locales successfully reloaded!");
                                return true;
                            }

                            return false;
                        })
                        .build()
                ).create());
        commandManager.enable();

        getLogger().info("igloolib initialized");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Listener registerListener(Supplier<Listener> listenerSupplier) {
        return LISTENERS.register(listenerSupplier);
    }
}
