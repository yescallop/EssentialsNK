package cn.yescallop.essentialsnk.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandMap;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.defaults.*;


public class CommandManager {

    public static void registerAll(EssentialsNK plugin) {
        CommandMap map = plugin.getServer().getCommandMap();
        map.register("essentialsnk", new BackCommand(plugin));
        map.register("essentialsnk", new BroadcastCommand(plugin));
        map.register("essentialsnk", new BurnCommand(plugin));
        map.register("essentialsnk", new ExtinguishCommand(plugin));
    }
}