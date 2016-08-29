package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class DepthCommand extends CommandBase {

    public DepthCommand(EssentialsNK plugin) {
        super("depth", plugin);
        this.setAliases(new String[]{"height"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return true;
        }
        if (args.length != 0) {
            return false;
        }
        sender.sendMessage(lang.translateString("commands.depth.success", String.valueOf(((Player) sender).getFloorY() - 63)));
        return true;
    }
}
