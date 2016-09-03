package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class BackCommand extends CommandBase {

    public BackCommand(EssentialsNK plugin) {
        super("back", plugin);
        this.setAliases(new String[]{"return"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }
        if (args.length != 0) {
            this.sendUsage(sender);
            return false;
        }
        Player player = (Player) sender;
        Location pos = plugin.getPlayerLastLocation(player);
        if (pos == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.back.notavalible"));
            return false;
        }
        player.teleport(pos);
        sender.sendMessage(lang.translateString("commands.back.success"));
        return true;
    }
}
