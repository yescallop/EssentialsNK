package cn.yescallop.essentialsnk.command.defaults.teleport;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class TPHereCommand extends CommandBase {

    public TPHereCommand(EssentialsNK plugin) {
        super("tphere", plugin);
        this.setAliases(new String[]{"s"});
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
        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notFound", args[0]));
            return false;
        }
        player.teleport((Player) sender);
        player.sendMessage(lang.translateString("commands.tphere.other"));
        sender.sendMessage(lang.translateString("commands.tphere.success"));
        return true;
    }
}
