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

        if (!this.testIngame(sender)) {
            return false;
        }

        if (args.length != 1) {
            this.sendUsage(sender);
            return false;
        }

        Player p = (Player) sender;

        int maxHomes = getPermissionValue(p, getPermission());

        if (maxHomes >= 0) {
            int homes = api.getHomesList(p).length;

            if (homes >= maxHomes) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.sethome.limit", maxHomes));
                return false;
            }
        }

        if (args[0].toLowerCase().equals("bed")) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.sethome.bed"));
            return false;
        } else if (args[0].trim().equals("")) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.sethome.empty"));
            return false;
        }

        sender.sendMessage(TextFormat.GREEN + (api.setHome(p, args[0].toLowerCase(), p) ? lang.translateString("commands.sethome.updated", args[0]) : lang.translateString("commands.sethome.success", args[0])));
        return true;
    }
}
