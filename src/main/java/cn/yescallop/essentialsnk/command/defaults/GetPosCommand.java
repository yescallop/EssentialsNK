package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class GetPosCommand extends CommandBase {

    public GetPosCommand(EssentialsAPI api) {
        super("getpos", api);
        this.setAliases(new String[]{"coords", "position", "whereami", "getlocation", "getloc"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Player player;
        if (args.length == 0) {
            if (!this.testIngame(sender)) {
                return false;
            }
            player = (Player) sender;
        } else {
            if (!sender.hasPermission("essentialsnk.getpos.others")) {
                this.sendPermissionMessage(sender);
                return false;
            }
            player = api.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        sender.sendMessage(sender == player ? lang.translateString("commands.getpos.success", player.getLevel().getName(), String.valueOf(player.getFloorX()), String.valueOf(player.getFloorY()), String.valueOf(player.getFloorZ())) : lang.translateString("commands.getpos.success.other", player.getDisplayName(), player.getLevel().getName(), String.valueOf(player.getFloorX()), String.valueOf(player.getFloorY()), String.valueOf(player.getFloorZ())));
        return true;
    }
}
