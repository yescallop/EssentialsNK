package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class ItemDBCommand extends CommandBase {

    public ItemDBCommand(EssentialsAPI api) {
        super("itemdb", api);
        this.setAliases(new String[]{"itemno", "durability", "dura"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Item item = ((Player) sender).getInventory().getItemInHand();
        String message = api.isRepairable(item) ? lang.translateString("commands.itemdb.damage", String.valueOf(item.getDamage())) : lang.translateString("commands.itemdb.meta", String.valueOf(item.getDamage()));
        if (args.length == 1) {
            switch (args[0]) {
                case "name":
                    message = lang.translateString("commands.itemdb.name", item.getName());
                    break;
                case "id":
                    message = lang.translateString("commands.itemdb.id", String.valueOf(item.getId()));
                    break;
            }
        }
        sender.sendMessage(message);
        return true;
    }
}
