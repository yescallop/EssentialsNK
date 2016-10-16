package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class ClearInventoryCommand extends CommandBase {

    public ClearInventoryCommand(EssentialsAPI api) {
        super("clearinventory", api);
        this.setAliases(new String[]{"ci", "clean", "clearinvent"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Player player;
        if (args.length == 0) {
            if (!this.testIngame(sender)) {
                return false;
            }
            player = (Player) sender;
        } else {
            if (!sender.hasPermission("essentialsnk.clearinventory.others")) {
                this.sendPermissionMessage(sender);
                return false;
            }
            player = api.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        player.getInventory().clearAll();
        player.sendMessage(lang.translateString("commands.clearinventory.success"));
        if (sender != player) {
            sender.sendMessage(lang.translateString("commands.clearinventory.success.other", player.getDisplayName()));
        }
        return true;
    }
}
