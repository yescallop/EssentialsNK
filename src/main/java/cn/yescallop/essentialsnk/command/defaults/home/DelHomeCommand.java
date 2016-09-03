package cn.yescallop.essentialsnk.command.defaults.home;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class DelHomeCommand extends CommandBase {

    public DelHomeCommand(EssentialsNK plugin) {
        super("delhome", plugin);
        this.setAliases(new String[]{"remhome", "removehome"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }
        if (args.length != 1) {
            this.sendUsage(sender);
            return false;
        }
        if (!plugin.isHomeExists((Player) sender, args[0].toLowerCase())) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.delhome.notexists"));
            return false;
        }
        plugin.removeHome((Player) sender, args[0].toLowerCase());
        sender.sendMessage(lang.translateString("commands.delhome.success"));
        return true;
    }
}
