package cn.yescallop.essentialsnk.command.defaults.warp;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class DelWarpCommand extends CommandBase {

    public DelWarpCommand(EssentialsNK plugin) {
        super("delwarp", plugin);
        this.setAliases(new String[]{"remwarp", "removewarp", "closewarp"});
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
        if (!plugin.isWarpExists(args[0].toLowerCase())) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.delwarp.notexists"));
            return false;
        }
        plugin.removeWarp(args[0].toLowerCase());
        sender.sendMessage(lang.translateString("commands.delwarp.success"));
        return true;
    }
}
