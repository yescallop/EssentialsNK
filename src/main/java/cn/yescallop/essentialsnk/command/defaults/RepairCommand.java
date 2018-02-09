package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

import java.util.Map;

public class RepairCommand extends CommandBase {

    public RepairCommand(EssentialsAPI api) {
        super("repair", api);
        this.setAliases(new String[]{"fix"});
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
        Player player = (Player) sender;
        switch (args[0]) {
            case "all":
                if (!sender.hasPermission("essentialsnk.repair.all")) {
                    this.sendPermissionMessage(sender);
                    return false;
                }
                Map<Integer, Item> contents = player.getInventory().getContents();
                for (Item item : contents.values()) {
                    if (api.isRepairable(item)) {
                        item.setDamage(0);
                    }
                }
                player.getInventory().setContents(contents);
                if (sender.hasPermission("essentialsnk.repair.armor")) {
                    Item[] armors = player.getInventory().getArmorContents();
                    for (Item item : armors) {
                        if (api.isRepairable(item)) {
                            item.setDamage(0);
                        }
                    }
                    player.getInventory().setArmorContents(armors);
                    sender.sendMessage(Language.translate("commands.repair.armor"));
                } else {
                    sender.sendMessage(Language.translate("commands.repair.all"));
                }
                break;
            case "hand":
                Item item = player.getInventory().getItemInHand();
                if (!api.isRepairable(item)) {
                    sender.sendMessage(Language.translate("commands.repair.unrepairable"));
                    return false;
                }
                item.setDamage(0);
                player.getInventory().setItemInHand(item);
                sender.sendMessage(Language.translate("commands.repair.success"));
                break;
            default:
                this.sendUsage(sender);
                return false;
        }
        return true;
    }
}
