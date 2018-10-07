package cn.yescallop.essentialsnk.command.defaults.teleport;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.TPRequest;
import cn.yescallop.essentialsnk.command.CommandBase;

public class TPAcceptCommand extends CommandBase {

    public TPAcceptCommand(EssentialsAPI api) {
        super("tpaccept", api);
        this.setAliases(new String[]{"tpyes"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Player to = (Player) sender;
        if (api.getLatestTPRequestTo(to) == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.tpaccept.noRequest"));
            return false;
        }
        TPRequest request;
        Player from;
        if (args.length == 0) {
            if ((request = api.getLatestTPRequestTo(to)) == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.tpaccept.unavailable"));
                return false;
            }
            from = request.getFrom();
        } else {
            from = api.getServer().getPlayer(args[0]);
            if (from == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
                return false;
            }
            if ((request = api.getTPRequestBetween(from, to)) != null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.tpaccept.noRequestFrom", from.getDisplayName()));
                return false;
            }
        }
        from.sendMessage(TextFormat.YELLOW + lang.translateString("commands.tpaccept.accepted", to.getDisplayName()));
        sender.sendMessage(TextFormat.YELLOW + lang.translateString("commands.generic.teleporting"));
        if (request.isTo()) {
            from.teleport(to);
        } else {
            to.teleport(from);
        }
        api.removeTPRequestBetween(from, to);
        return true;
    }
}
