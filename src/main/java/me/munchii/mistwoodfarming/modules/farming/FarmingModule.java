package me.munchii.mistwoodfarming.modules.farming;

import me.munchii.igloolib.command.IglooCommand;
import me.munchii.igloolib.module.PluginModule;
import me.munchii.igloolib.util.Chat;
import me.munchii.mistwoodfarming.MistwoodFarming;
import me.munchii.mistwoodfarming.config.MistwoodFarmingConfig;

public class FarmingModule extends PluginModule {
    public FarmingModule() {
        super("farming", MistwoodFarmingConfig.farmingModuleEnabled);
    }

    @Override
    public void onEnable() {
        getCommandManager()
                .registerCommand(IglooCommand.create("help")
                        .withDescription("Displays this help message")
                        .withUsage("/help")
                        .withAction((sender, args) -> {
                            sender.sendMessage(Chat.color("&8--- &aFarming Help &8---"));
                            getCommandManager().getCommands().forEach(command -> {
                                sender.sendMessage(Chat.color(" &8- &a" + command.getUsage() + " &7" + command.getDescription()));
                            });
                            getCommandManager().getCommandGroups().forEach(group -> {
                                group.getSubCommands().forEach(command -> {
                                    sender.sendMessage(Chat.color(" &8- &a/" + group.getGroupCommand() + " " + command.getUsage().replace("/", "") + " &7" + command.getDescription()));
                                });
                            });
                            return true;
                        })
                        .build());
    }

    @Override
    public void onDisable() {

    }
}
