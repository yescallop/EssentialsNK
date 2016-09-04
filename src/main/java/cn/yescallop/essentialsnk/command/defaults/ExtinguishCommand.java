package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class ExtinguishCommand extends CommandBase {

    public ExtinguishCommand(EssentialsAPI api) {
        super("extinguish", api);
        this.setAliases(new String[]{"ext"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        Player player;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
                return false;
            }
            player = (Player) sender;
        } else if (args.length == 1) {
            if (!sender.hasPermission("essentialsnk.extinguish.other")) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return false;
            }
            player = api.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
                return false;
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
