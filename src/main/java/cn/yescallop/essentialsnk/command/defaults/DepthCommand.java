package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class DepthCommand extends CommandBase {

    public DepthCommand(EssentialsAPI api) {
        super("depth", api);
        this.setAliases(new String[]{"height"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length != 0) {
            this.sendUsage(sender);
            return false;
        }
        sender.sendMessage(Language.translate("commands.depth.success", String.valueOf(((Player) sender).getFloorY() - 63)));
        return true;
    }
}
