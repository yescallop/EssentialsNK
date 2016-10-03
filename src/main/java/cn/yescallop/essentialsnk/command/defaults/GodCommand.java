package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.PlayerData;
import cn.yescallop.essentialsnk.command.CommandBase;

public class GodCommand extends CommandBase {

    public GodCommand(EssentialsAPI api) {
        super("god", api);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }

        if (args.length > 2) {
            this.sendUsage(sender);
            return false;
        }

        Player player = null;
        Boolean value = null;

        if (args.length == 2) {
            player = getAPI().getServer().getPlayer(args[0]);

            if (sender != player && !sender.hasPermission("essentialsnk.god.others")) {
                sender.sendMessage(getPermissionMessage());
                return false;
            }

            if (player == null || !player.isOnline()) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound"));
                return false;
            }

            value = args[1].toLowerCase().equals("on");
        } else if (args.length == 1) {
            if (args[0].length() < 4) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
                    return false;
                }

                player = (Player) sender;
                value = args[0].toLowerCase().equals("on");
            } else {
                player = getAPI().getServer().getPlayer(args[0]);

                if (sender != player && !sender.hasPermission("essentialsnk.god.others")) {
                    sender.sendMessage(getPermissionMessage());
                    return false;
                }

                if (player == null || !player.isOnline()) {
                    sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound"));
                    return false;
                }
            }
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }

        PlayerData data = getAPI().getPlayerData(player);
        data.godMode = value == null ? !data.godMode : value;

        sender.sendMessage(lang.translateString(data.godMode ? "commands.god.enable" : "commands.god.disable"));
        return true;
    }
}
