package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
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
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
                return true;
            }
            player = (Player) sender;
        } else if (args.length == 1) {
            if (!sender.hasPermission("essentialsnk.extinguish.other")) {
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
        player.extinguish();
        sender.sendMessage(sender == player ? lang.translateString("commands.extinguish.success") : lang.translateString("commands.extinguish.success.other", player.getName()));
        return true;
    }
}
