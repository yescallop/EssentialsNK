package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class CompassCommand extends CommandBase {

    public CompassCommand(EssentialsAPI api) {
        super("compass", api);
        this.setAliases(new String[]{"direction"});
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
        String direction = ((Player) sender).getDirection().name().toLowerCase();

        sender.sendMessage(lang.translateString("commands.compass.success", lang.translateString("commands.compass." + direction)));
        return true;
    }
}
