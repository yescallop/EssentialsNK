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
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        plugin.getServer().broadcastMessage(this.parseMessage(args));
        return true;
    }
    
    private String parseMessage(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (builder.length() != 0) {
                builder.append(" ");
            }
            builder.append(args[i]);
        }
        return builder.toString();
    }
}
