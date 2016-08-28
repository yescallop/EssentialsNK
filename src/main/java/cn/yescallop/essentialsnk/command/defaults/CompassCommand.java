package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class CompassCommand extends CommandBase {

    public CompassCommand(EssentialsNK plugin) {
        super("compass", plugin);
        this.setAliases(new String[]{"direction"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("generic.ingame"));
            return true;
        }
        if (args.length != 0) {
            return false;
        }
        String direction;
        switch (((Player) sender).getDirection()) {
            case 0:
                direction = "south";
                break;
            case 1:
                direction = "west";
                break;
            case 2:
                direction = "north";
                break;
            case 3:
                direction = "east";
                break;
            default:
                direction = "error";
        }
        sender.sendMessage(lang.translateString("compass.success", lang.translateString("compass." + direction)));
        return true;
    }
}
