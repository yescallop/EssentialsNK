package cn.yescallop.essentialsnk.command.defaults.teleport;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class TPAHereCommand extends CommandBase {

    public TPAHereCommand(EssentialsAPI api) {
        super("tpahere", api);
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
        Player player = api.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
            return false;
        }
        if (sender == player) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.tpa.self"));
            return false;
        }
        api.requestTP((Player) sender, player, false);
        player.sendMessage(TextFormat.YELLOW + lang.translateString("commands.tpahere.invite", ((Player) sender).getName()));
        sender.sendMessage(TextFormat.GREEN + lang.translateString("commands.tpa.success", player.getDisplayName()));
        return true;
    }
}
