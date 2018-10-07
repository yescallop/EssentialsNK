package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class TopCommand extends CommandBase {

    public TopCommand(EssentialsAPI api) {
        super("top", api);
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
        Player player = (Player) sender;

        Position pos = api.getHighestStandablePositionAt(player);
        if (pos == null) {
            sender.sendMessage(lang.translateString("commands.top.failed"));
            return false;
        }

        sender.sendMessage(lang.translateString("commands.generic.teleporting"));
        player.teleport(pos);
        return true;
    }
}
