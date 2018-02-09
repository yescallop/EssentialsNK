package cn.yescallop.essentialsnk.command.defaults.home;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class SetHomeCommand extends CommandBase {

    public SetHomeCommand(EssentialsAPI api) {
        super("sethome", api);
        this.setAliases(new String[]{"createhome"});
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
        if (args[0].toLowerCase().equals("bed")) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.sethome.bed"));
            return false;
        } else if (args[0].trim().equals("")) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.sethome.empty"));
            return false;
        }
        sender.sendMessage(api.setHome((Player) sender, args[0].toLowerCase(), (Player) sender) ? Language.translate("commands.sethome.updated", args[0]) : Language.translate("commands.sethome.success", args[0]));
        return true;
    }
}
