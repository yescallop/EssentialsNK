package cn.yescallop.essentialsnk.command.defaults.warp;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class SetWarpCommand extends CommandBase {

    public SetWarpCommand(EssentialsAPI api) {
        super("setwarp", api);
        this.setAliases(new String[]{"createwarp"});
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
        if (args[0].trim().equals("")) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.setwarp.empty"));
            return false;
        }
        sender.sendMessage(api.setWarp(args[0].toLowerCase(), (Player) sender) ? Language.translate("commands.setwarp.replaced", args[0]) : Language.translate("commands.setwarp.success", args[0]));
        return true;
    }
}
