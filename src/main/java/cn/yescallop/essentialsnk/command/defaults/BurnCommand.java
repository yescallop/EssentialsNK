package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class BurnCommand extends CommandBase {

    public BurnCommand(EssentialsNK plugin) {
        super("burn", plugin);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length != 2) {
            this.sendUsage(sender);
            return false;
        }
        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
            return false;
        }
        int time;
        try {
            time = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.number.invalidinteger", args[1]));
            return false;
        }
        player.setOnFire(time);
        sender.sendMessage(lang.translateString("commands.burn.success", player.getName()));
        return true;
    }
}
