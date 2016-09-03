package cn.yescallop.essentialsnk.command.defaults.warp;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class WarpCommand extends CommandBase {

    public WarpCommand(EssentialsNK plugin) {
        super("warp", plugin);
        this.setAliases(new String[]{"warps"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            String[] list = plugin.getWarpsList();
            if (list.length == 0) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.warp.nowarp"));
                return false;
            }
            sender.sendMessage(lang.translateString("commands.warp.list") + "\n" + plugin.implode(list, ", "));
            return true;
        }
        Location warp = plugin.getWarp(args[0].toLowerCase());
        if (warp == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.warp.notexists"));
            return false;
        }
        player.teleport(warp);
        sender.sendMessage(lang.translateString("commands.warp.success", args[0]));
        return true;
    }
}
