package cn.yescallop.essentialsnk.command.defaults.home;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class DelHomeCommand extends CommandBase {

    public DelHomeCommand(EssentialsAPI api) {
        super("delhome", api);
        this.setAliases(new String[]{"remhome", "rmhome"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length != 1) {
            this.sendUsage(sender);
            return false;
        }
        if (!api.isHomeExists((Player) sender, args[0].toLowerCase())) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.home.notexists", args[0]));
            return false;
        }
        api.removeHome((Player) sender, args[0].toLowerCase());
        sender.sendMessage(Language.translate("commands.delhome.success", args[0]));
        return true;
    }
}
