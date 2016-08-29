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
            return true;
        }
        if (args.length != 2) {
            return false;
        }
        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notFound", args[0]));
            return true;
        }
        int time;
        try {
            time = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.number.invalidInteger", args[1]));
            return true;
        }
        player.setOnFire(time);
        sender.sendMessage(lang.translateString("commands.burn.success", player.getName()));
        return true;
    }
}
