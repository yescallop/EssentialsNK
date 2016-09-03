package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class BroadcastCommand extends CommandBase {

    public BroadcastCommand(EssentialsNK plugin) {
        super("broadcast", plugin);
        this.setAliases(new String[]{"bcast"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length == 0) {
            this.sendUsage(sender);
            return false;
        }
        plugin.getServer().broadcastMessage(plugin.parseMessage(args));
        return true;
    }
}
