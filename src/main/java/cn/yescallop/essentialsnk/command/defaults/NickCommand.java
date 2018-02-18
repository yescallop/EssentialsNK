package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class NickCommand extends CommandBase {

    public NickCommand(EssentialsAPI api) {
        super("nick", api);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length > 2) {
            this.sendUsage(sender);
            return false;
        }
        Player player;
        if (args.length == 1) {
            if (!this.testIngame(sender)) {
                return false;
            }
            player = (Player) sender;
        } else {
            if (!sender.hasPermission("essentialsnk.nick.others")) {
                this.sendPermissionMessage(sender);
                return false;
            }
            player = api.getServer().getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        String nick = TextFormat.colorize('&',args[0]);
        api.setNick(player,nick);
        player.sendMessage(Language.translate("commands.nick.success", nick));
        if (sender != player) {
            sender.sendMessage(Language.translate("commands.nick.success.other", player.getDisplayName(), nick));
        }
        return true;
    }
}
