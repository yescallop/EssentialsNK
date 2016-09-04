package cn.yescallop.essentialsnk.command.defaults.home;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }
        if (args.length != 1) {
            this.sendUsage(sender);
            return false;
        }
        if (args[0].toLowerCase().equals("bed")) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.sethome.bed"));
            return false;
        } else if (args[0].trim().equals("")) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.sethome.empty"));
            return false;
        }
        sender.sendMessage(api.setHome((Player) sender, args[0].toLowerCase(), (Player) sender) ? lang.translateString("commands.sethome.replaced") : lang.translateString("commands.sethome.success"));
        return true;
    }
}
