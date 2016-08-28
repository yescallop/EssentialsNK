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
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("generic.ingame"));
            return true;
        }
        if (args.length > 0) {
            return false;
        }
        Player player = (Player) sender;
        Location pos = plugin.getPlayerLastLocation(player);
        if (pos == null) {
            sender.sendMessage(lang.translateString("back.notavalible"));
            return true;
        }
        player.teleport(pos);
        sender.sendMessage(lang.translateString("back.success"));
        return true;
    }
}
