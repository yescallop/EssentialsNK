package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class MoreCommand extends CommandBase {

    public MoreCommand(EssentialsAPI api) {
        super("more", api);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length != 0) {
            this.sendUsage(sender);
            return false;
        }
        Player player = (Player) sender;
        if (player.isCreative() || player.isSpectator()) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.more.notavalible"));
            return false;
        }
        Item item = player.getInventory().getItemInHand();
        if (item.getId() == Item.AIR) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.more.air"));
            return false;
        }
        item.setCount(item.getMaxStackSize());
        player.getInventory().setItemInHand(item);
        sender.sendMessage(Language.translate("commands.more.success"));
        return true;
    }
}
