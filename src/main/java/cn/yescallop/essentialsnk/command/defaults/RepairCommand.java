package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class RepairCommand extends CommandBase {

    public RepairCommand(EssentialsNK plugin) {
        super("repair", plugin);
        this.setAliases(new String[]{"fix"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }
        if (args.length != 1) {
            this.sendUsage(sender);
            return false;
        }
        Player player = (Player) sender;
        switch (args[0]) {
            case "all":
                if (!sender.hasPermission("essentialsnk.repair.all")) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                    return false;
                }
                for (Item item : player.getInventory().getContents().values()) {
                    if (plugin.isRepairable(item)) {
                        item.setDamage(0);
                    }
                }
                if (sender.hasPermission("essentialsnk.repair.armor")) {
                    for (Item item : player.getInventory().getArmorContents()) {
                        if (plugin.isRepairable(item)) {
                            item.setDamage(0);
                        }
                    }
                    sender.sendMessage(lang.translateString("commands.repair.armor"));
                } else {
                    sender.sendMessage(lang.translateString("commands.repair.all"));
                }
                break;
            case "hand":
                Item item = player.getInventory().getItemInHand();
                if (!plugin.isRepairable(item)) {
                    sender.sendMessage(lang.translateString("commands.repair.unrepairable"));
                    return false;
                }
                item.setDamage(0);
                sender.sendMessage(lang.translateString("commands.repair.success"));
                break;
            default:
                this.sendUsage(sender);
                return false;
        }
        return true;
    }
}
