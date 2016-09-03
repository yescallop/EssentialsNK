package cn.yescallop.essentialsnk.command.defaults.warp;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class SetWarpCommand extends CommandBase {

    public SetWarpCommand(EssentialsNK plugin) {
        super("setwarp", plugin);
        this.setAliases(new String[]{"createwarp"});
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
        if (args[0].trim().equals("")) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.setwarp.empty"));
            return false;
        }
        sender.sendMessage(plugin.setWarp(args[0].toLowerCase(), (Player) sender) ? lang.translateString("commands.setwarp.replaced") : lang.translateString("commands.setwarp.success"));
        return true;
    }
}
