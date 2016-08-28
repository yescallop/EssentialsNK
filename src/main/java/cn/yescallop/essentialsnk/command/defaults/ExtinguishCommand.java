package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class ExtinguishCommand extends CommandBase {

    public ExtinguishCommand(EssentialsNK plugin) {
        super("extinguish", plugin);
        this.setAliases(new String[]{"ext"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        Player player;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextFormat.RED + lang.translateString("generic.ingame"));
                return true;
            }
            player = (Player) sender;
        } else if (args.length == 1) {
            player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("generic.player.notFound", args[0]));
                return true;
            }
        } else {
            return false;
        }
        player.extinguish();
        sender.sendMessage(sender == player ? lang.translateString("extinguish.success") : lang.translateString("extinguish.success.other", player.getName()));
        return true;
    }
}
