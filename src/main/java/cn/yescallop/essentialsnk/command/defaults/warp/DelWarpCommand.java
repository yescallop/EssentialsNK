package cn.yescallop.essentialsnk.command.defaults.warp;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class DelWarpCommand extends CommandBase {

    public DelWarpCommand(EssentialsAPI api) {
        super("delwarp", api);
        this.setAliases(new String[]{"remwarp", "rmwarp"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length != 1) {
            this.sendUsage(sender);
            return false;
        }
        if (!api.isWarpExists(args[0].toLowerCase())) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.warp.notexists"));
            return false;
        }
        api.removeWarp(args[0].toLowerCase());
        sender.sendMessage(lang.translateString("commands.delwarp.success"));
        return true;
    }
}
