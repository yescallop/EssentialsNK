package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class BurnCommand extends CommandBase {

    public BurnCommand(EssentialsAPI api) {
        super("burn", api);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length != 2) {
            this.sendUsage(sender);
            return false;
        }
        Player player = api.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
            return false;
        }
        int time;
        try {
            time = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.number.invalidinteger", args[1]));
            return false;
        }
        if (time <= 0) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.number.invalidinteger", args[1]));
            return false;
        }
        player.setOnFire(time);
        sender.sendMessage(Language.translate("commands.burn.success", player.getDisplayName()));
        return true;
    }
}
