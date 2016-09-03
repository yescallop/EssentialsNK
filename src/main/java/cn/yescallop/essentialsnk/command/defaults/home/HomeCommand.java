package cn.yescallop.essentialsnk.command.defaults.home;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class HomeCommand extends CommandBase {

    public HomeCommand(EssentialsNK plugin) {
        super("home", plugin);
        this.setAliases(new String[]{"homes"});
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
        Player player = (Player) sender;
        if (args.length == 0) {
            String[] list = plugin.getHomesList(player);
            if (list.length == 0) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.home.nohome"));
                return false;
            }
            sender.sendMessage(lang.translateString("commands.home.list") + "\n" + plugin.implode(list, ", "));
            return true;
        }
        Location home = plugin.getHome(player, args[0].toLowerCase());
        if (home == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.home.notexists"));
            return false;
        }
        player.teleport(home);
        sender.sendMessage(lang.translateString("commands.home.success", args[0]));
        return true;
    }
}
