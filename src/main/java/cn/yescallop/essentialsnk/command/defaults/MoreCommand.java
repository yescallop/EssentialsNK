package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class MoreCommand extends CommandBase {

    public MoreCommand(EssentialsNK plugin) {
        super("more", plugin);
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
        Player player = (Player) sender;
        if (player.isCreative() || player.isSpectator()) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.more.notavalible"));
            return true;
        }
        Item item = player.getInventory().getItemInHand();
        if (item.getId() == Item.AIR) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.more.air"));
            return true;
        }
        item.setCount(item.getMaxStackSize());
        sender.sendMessage(lang.translateString("commands.more.success"));
        return true;
    }
}
