package cn.yescallop.essentialsnk.command.defaults.warp;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
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
        if (args.length == 0) {
            String[] list = api.getWarpsList();
            if (list.length == 0) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.warp.nowarp"));
                return false;
            }
            sender.sendMessage(Language.translate("commands.warp.list") + "\n" + String.join(", ", list));
            return true;
        }
        
        Location warp = null;
        String warpname = "";
        Player target = null;
        
        if(args.length == 1){
            if (!this.testIngame(sender)) {
                return false;
            }
            target = (Player) sender;
            warpname = args[0];
            warp = api.getWarp(args[0].toLowerCase());  //:/warp <warpname>
        }else if(args.length == 2){
            if (!sender.hasPermission("essentialsnk.warp.others")) {
                this.sendPermissionMessage(sender);
                return false;
            }
            target = api.getServer().getPlayer(args[0]);//:/warp <Playername> <warpname>
            if (target == null) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
                return false;
            }
            warpname = args[1];
            warp = api.getWarp(args[1].toLowerCase());  //:/warp <Playername> <warpname>
        }else if (args.length > 2) {
            this.sendUsage(sender);
            return false;
        }
        
        if (warp == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.warp.notexists", args[0]));
            return false;
        }
        
        target.teleport(warp);
        target.sendMessage(Language.translate("commands.warp.success", warpname));
        if (sender != target) {
            target.sendMessage(Language.translate("commands.warp.success.other", new String[]{target.getDisplayName(), warpname}));
        }
        return true;
    }
}
