package cn.yescallop.essentialsnk.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandMap;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.defaults.*;
import cn.yescallop.essentialsnk.command.defaults.teleport.*;


public class CommandManager {

    public static void registerAll(EssentialsNK plugin) {
        CommandMap map = plugin.getServer().getCommandMap();
        map.register("essentialsnk", new BackCommand(plugin));
        map.register("essentialsnk", new BreakCommand(plugin));
        map.register("essentialsnk", new BroadcastCommand(plugin));
        map.register("essentialsnk", new BurnCommand(plugin));
        map.register("essentialsnk", new ClearInventoryCommand(plugin));
        map.register("essentialsnk", new CompassCommand(plugin));
        map.register("essentialsnk", new DepthCommand(plugin));
        map.register("essentialsnk", new ExtinguishCommand(plugin));
        map.register("essentialsnk", new FlyCommand(plugin));
        map.register("essentialsnk", new GetPosCommand(plugin));
        map.register("essentialsnk", new HealCommand(plugin));
        map.register("essentialsnk", new ItemDBCommand(plugin));
        map.register("essentialsnk", new JumpCommand(plugin));
        map.register("essentialsnk", new KickAllCommand(plugin));
        map.register("essentialsnk", new LightningCommand(plugin));
        map.register("essentialsnk", new MoreCommand(plugin));
        map.register("essentialsnk", new PingCommand(plugin));
        map.register("essentialsnk", new RepairCommand(plugin));
        map.register("essentialsnk", new VanishCommand(plugin));
        map.register("essentialsnk", new WorldCommand(plugin));
        
        map.register("essentialsnk", new TPACommand(plugin));
        map.register("essentialsnk", new TPAcceptCommand(plugin));
        map.register("essentialsnk", new TPAHereCommand(plugin));
    }
}