package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;


public class RenameCommand extends CommandBase {

    public RenameCommand(EssentialsAPI api) {
        super("rename", api);
        this.setAliases(new String[]{"renameme"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }

        if (sender instanceof Player){

            Player player = (Player) sender;
            Item item = player.getInventory().getItemInHand();

            if (item != null){

                StringBuilder newname = new StringBuilder();

                for (String arg : args){
                    newname.append(arg).append(" ");
                }

                if (newname.length() < 50){
                    item.setCustomName(newname.toString());
                    player.getInventory().setItemInHand(item);
                    sender.sendMessage("debug2");

                }
            }
        }
        return true;
    }
}
