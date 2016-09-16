package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class MuteCommand extends CommandBase {

    public MuteCommand(EssentialsAPI api) {
        super("mute", api);
        this.setAliases(new String[]{"silence"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(args.length == 1 || args.length == 4)) {
            this.sendUsage(sender);
            return false;
        }
        Player player = api.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
            return false;
        }
        if (sender == player) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.mute.self"));
            return false;
        }
        if (args.length == 4) {
            int d;
            try {
                d = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.number.invalidinteger", args[1]));
                return false;
            }
            int h;
            try {
                h = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.number.invalidinteger", args[2]));
                return false;
            }
            int m;
            try {
                m = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.number.invalidinteger", args[3]));
                return false;
            }
            if (d == 0 && h == 0 && m == 0) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.mute.zero"));
                return false;
            }
            if (!api.mute(player, d, h, m)) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.mute.range"));
                return false;
            }
            String message = api.getMuteTimeMessage(d, h, m);
            sender.sendMessage(lang.translateString("commands.mute.success", player.getDisplayName(), message));
            player.sendMessage(lang.translateString("commands.mute.other", message));
        } else {
            api.unmute(player);
            sender.sendMessage(lang.translateString("commands.mute.unmute.success", player.getDisplayName()));
            player.sendMessage(lang.translateString("commands.mute.unmute.other"));
        }
        return true;
    }
}
