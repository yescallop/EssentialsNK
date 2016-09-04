package cn.yescallop.essentialsnk.command.defaults.warp;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class WarpCommand extends CommandBase {

    public WarpCommand(EssentialsAPI api) {
        super("warp", api);
        this.setAliases(new String[]{"warps"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length > 2) {
            this.sendUsage(sender);
            return false;
        }
        if (args.length == 0) {
            String[] list = api.getWarpsList();
            if (list.length == 0) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.warp.nowarp"));
                return false;
            }
            sender.sendMessage(lang.translateString("commands.warp.list") + "\n" + api.implode(list, ", "));
            return true;
        }
        Location warp = api.getWarp(args[0].toLowerCase());
        if (warp == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.warp.notexists"));
            return false;
        }
        Player player;
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
                return false;
            }
            player = (Player) sender;
        } else {
            if (!sender.hasPermission("essentialsnk.warp.other")) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return false;
            }
            player = api.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        player.teleport(warp);
        player.sendMessage(lang.translateString("commands.warp.success", args[0]));
        if (sender != player) {
            player.sendMessage(lang.translateString("commands.warp.success.other", new String[]{player.getDisplayName(), args[0]}));
        }
        return true;
    }
}