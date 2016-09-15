package cn.yescallop.essentialsnk.command.defaults.teleport;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class TPAAllCommand extends CommandBase {

    public TPAAllCommand(EssentialsAPI api) {
        super("tpaall", api);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        Player player;
        if (args.length == 0) {
            if (!this.testIngame(sender)) {
                return false;
            }
            player = (Player) sender;
        } else if (args.length == 1) {
            player = api.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
                return false;
            }
        } else {
            this.sendUsage(sender);
            return false;
        }
        for (Player p : api.getServer().getOnlinePlayers().values()) {
            if (p != player) {
                api.requestTP(player, p, false);
                p.sendMessage(lang.translateString("commands.tpahere.invite", player.getDisplayName()));
            }
        }
        player.sendMessage(lang.translateString("commands.tpaall.success"));
        return true;
    }
}
