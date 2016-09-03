package cn.yescallop.essentialsnk.command.defaults.teleport;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.TPRequest;
import cn.yescallop.essentialsnk.command.CommandBase;

public class TPDenyCommand extends CommandBase {

    public TPDenyCommand(EssentialsNK plugin) {
        super("tpdeny", plugin);
        this.setAliases(new String[]{"tpno"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Player to = (Player) sender;
        if (plugin.getLatestTPRequestTo(to) == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.tpdeny.noRequest"));
            return false;
        }
        TPRequest request;
        Player from;
        switch (args.length) {
            case 0:
                if((request = plugin.getLatestTPRequestTo(to)) == null) {
                    sender.sendMessage(TextFormat.RED + lang.translateString("commands.tpdeny.unavailable"));
                    return false;
                }
                from = request.getFrom();
                break;
            case 1:
                from = plugin.getServer().getPlayer(args[0]);
                if (from == null) {
                    sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notFound", args[0]));
                    return false;
                }
                if ((request = plugin.getTPRequestBetween(from, to)) != null) {
                    sender.sendMessage(TextFormat.RED + lang.translateString("commands.tpdeny.noRequestFrom", from.getName()));
                    return false;
                }
                break;
            default:
                return false;
        }
        from.sendMessage(lang.translateString("commands.tpdeny.denied"));
        sender.sendMessage(lang.translateString("commands.tpdeny.success"));
        plugin.removeTPRequestBetween(from, to);
        return true;
    }
}
