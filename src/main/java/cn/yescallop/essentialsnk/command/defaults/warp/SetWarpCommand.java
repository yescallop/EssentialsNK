package cn.yescallop.essentialsnk.command.defaults.warp;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;
import cn.yescallop.essentialsnk.data.Warp;

public class SetWarpCommand extends CommandBase {

    public SetWarpCommand(EssentialsAPI api) {
        super("setwarp", api);
        this.setAliases(new String[]{"createwarp"});
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

        String warp = args[0];

        if (warp.trim().equals("")) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.setwarp.empty"));
            return false;
        }

        Player p = (Player) sender;

        int maxWarps = getPermissionValue(p, getPermission());

        if (maxWarps >= 0) {
            int warps = api.getWarpsList(p).length;

            if (warps >= maxWarps) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.setwarp.limit", maxWarps));
                return false;
            }
        }

        Warp existing = api.getWarp(warp);

        if (existing != null && !existing.getCreator().equalsIgnoreCase(p.getName()) && !sender.hasPermission("essentialsnk.setwarp.others")) {
            sender.sendMessage(lang.translateString("commands.warp.edit.denied"));
            return false;
        }

        sender.sendMessage(TextFormat.YELLOW + (api.setWarp(warp.toLowerCase(), (Player) sender, sender.getName()) ? lang.translateString("commands.setwarp.replaced", warp) : lang.translateString("commands.setwarp.success", warp)));
        return true;
    }
}
