package cn.yescallop.essentialsnk.command;

import cn.nukkit.command.CommandMap;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.defaults.*;
import cn.yescallop.essentialsnk.command.defaults.home.DelHomeCommand;
import cn.yescallop.essentialsnk.command.defaults.home.HomeCommand;
import cn.yescallop.essentialsnk.command.defaults.home.SetHomeCommand;
import cn.yescallop.essentialsnk.command.defaults.teleport.*;
import cn.yescallop.essentialsnk.command.defaults.warp.DelWarpCommand;
import cn.yescallop.essentialsnk.command.defaults.warp.SetWarpCommand;
import cn.yescallop.essentialsnk.command.defaults.warp.WarpCommand;


public class CommandManager {

    public static void registerAll(EssentialsAPI api) {
        CommandMap map = api.getServer().getCommandMap();
        map.register("EssentialsNK", new BackCommand(api));
        map.register("EssentialsNK", new BreakCommand(api));
        map.register("EssentialsNK", new BroadcastCommand(api));
        map.register("EssentialsNK", new BurnCommand(api));
        map.register("EssentialsNK", new ClearInventoryCommand(api));
        map.register("EssentialsNK", new CompassCommand(api));
        map.register("EssentialsNK", new DepthCommand(api));
        map.register("EssentialsNK", new ExtinguishCommand(api));
        map.register("EssentialsNK", new FlyCommand(api));
        map.register("EssentialsNK", new GamemodeCommand(api));
        map.register("EssentialsNK", new GetPosCommand(api));
        map.register("EssentialsNK", new HealCommand(api));
        map.register("EssentialsNK", new ItemDBCommand(api));
        map.register("EssentialsNK", new JumpCommand(api));
        map.register("EssentialsNK", new KickAllCommand(api));
        map.register("EssentialsNK", new LightningCommand(api));
        map.register("EssentialsNK", new MoreCommand(api));
        map.register("EssentialsNK", new PingCommand(api));
        map.register("EssentialsNK", new RepairCommand(api));
        map.register("EssentialsNK", new VanishCommand(api));
        map.register("EssentialsNK", new WorldCommand(api));

        map.register("EssentialsNK", new DelHomeCommand(api));
        map.register("EssentialsNK", new HomeCommand(api));
        map.register("EssentialsNK", new SetHomeCommand(api));

        map.register("EssentialsNK", new TPACommand(api));
        map.register("EssentialsNK", new TPAcceptCommand(api));
        map.register("EssentialsNK", new TPAHereCommand(api));
        map.register("EssentialsNK", new TPAllCommand(api));
        map.register("EssentialsNK", new TPDenyCommand(api));
        map.register("EssentialsNK", new TPHereCommand(api));

        map.register("EssentialsNK", new DelWarpCommand(api));
        map.register("EssentialsNK", new WarpCommand(api));
        map.register("EssentialsNK", new SetWarpCommand(api));
    }
}