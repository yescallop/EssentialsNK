package cn.yescallop.essentialsnk.command;

import cn.nukkit.command.CommandMap;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.defaults.*;
import cn.yescallop.essentialsnk.command.defaults.home.*;
import cn.yescallop.essentialsnk.command.defaults.teleport.*;
import cn.yescallop.essentialsnk.command.defaults.warp.*;


public class CommandManager {

    public static void registerAll(CommandMap map, EssentialsAPI api) {
        map.register("essentialsnk", new BackCommand(api));
        map.register("essentialsnk", new BreakCommand(api));
        map.register("essentialsnk", new BroadcastCommand(api));
        map.register("essentialsnk", new BurnCommand(api));
        map.register("essentialsnk", new ClearInventoryCommand(api));
        map.register("essentialsnk", new CompassCommand(api));
        map.register("essentialsnk", new DepthCommand(api));
        map.register("essentialsnk", new ExtinguishCommand(api));
        map.register("essentialsnk", new FlyCommand(api));
        map.register("essentialsnk", new GetPosCommand(api));
        map.register("essentialsnk", new HealCommand(api));
        map.register("essentialsnk", new ItemDBCommand(api));
        map.register("essentialsnk", new JumpCommand(api));
        map.register("essentialsnk", new KickAllCommand(api));
        map.register("essentialsnk", new LightningCommand(api));
        map.register("essentialsnk", new MoreCommand(api));
        map.register("essentialsnk", new PingCommand(api));
        map.register("essentialsnk", new RepairCommand(api));
        map.register("essentialsnk", new VanishCommand(api));
        map.register("essentialsnk", new WorldCommand(api));
        
        map.register("essentialsnk", new DelHomeCommand(api));
        map.register("essentialsnk", new HomeCommand(api));
        map.register("essentialsnk", new SetHomeCommand(api));
        
        map.register("essentialsnk", new TPACommand(api));
        map.register("essentialsnk", new TPAcceptCommand(api));
        map.register("essentialsnk", new TPAHereCommand(api));
        map.register("essentialsnk", new TPAllCommand(api));
        map.register("essentialsnk", new TPDenyCommand(api));
        map.register("essentialsnk", new TPHereCommand(api));
        
        map.register("essentialsnk", new DelWarpCommand(api));
        map.register("essentialsnk", new WarpCommand(api));
        map.register("essentialsnk", new SetWarpCommand(api));
    }
}