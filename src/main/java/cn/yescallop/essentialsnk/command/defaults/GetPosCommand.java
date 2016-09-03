package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class GetPosCommand extends CommandBase {

    public GetPosCommand(EssentialsNK plugin) {
        super("getpos", plugin);
        this.setAliases(new String[]{"coords", "position", "whereami", "getlocation", "getloc"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        Player player;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
                return true;
            }
            player = (Player) sender;
        } else if (args.length == 1) {
            if (!sender.hasPermission("essentialsnk.getpos.other")) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return true;
            }
            player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notFound", args[0]));
                return true;
            }
        } else {
            this.sendUsage(sender);
            return false;
        }
        sender.sendMessage(sender == player ? lang.translateString("commands.getpos.success", new String[]{player.getLevel().getName(), String.valueOf(player.getFloorX()), String.valueOf(player.getFloorY()), String.valueOf(player.getFloorZ())}) : lang.translateString("commands.getpos.success.other", new String[]{player.getName(), player.getLevel().getName(), String.valueOf(player.getFloorX()), String.valueOf(player.getFloorY()), String.valueOf(player.getFloorZ())}));
        return true;
    }
}
