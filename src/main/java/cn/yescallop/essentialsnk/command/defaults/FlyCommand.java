package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class FlyCommand extends CommandBase {

    public FlyCommand(EssentialsNK plugin) {
        super("fly", plugin);
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
            if (!sender.hasPermission("essentialsnk.fly.other")) {
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
        boolean allow = plugin.switchAllowFlight(player);
        player.sendMessage(allow ? lang.translateString("commands.fly.enabled") : lang.translateString("commands.fly.disabled"));
        if (sender != player) {
            sender.sendMessage(allow ? lang.translateString("commands.fly.enabled.other", player.getName()) : lang.translateString("commands.fly.disabled.other", player.getName()));
        }
        return true;
    }
}
