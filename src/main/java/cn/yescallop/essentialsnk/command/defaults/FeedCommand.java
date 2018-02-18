package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.PlayerFood;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class FeedCommand extends CommandBase {

    public FeedCommand(EssentialsAPI api) {
        super("feed", api);
        this.setAliases(new String[]{"eat"});
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
            if (!sender.hasPermission("essentialsnk.feed.others")) {
                this.sendPermissionMessage(sender);
                return false;
            }
            player = api.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        PlayerFood foodData = player.getFoodData();
        foodData.setLevel(foodData.getMaxLevel());
        foodData.sendFoodLevel();
        player.sendMessage(Language.translate("commands.feed.success"));
        if (sender != player) {
            sender.sendMessage(Language.translate("commands.feed.success.other", player.getDisplayName()));
        }
        return true;
    }
}
