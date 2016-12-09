package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

/**
 * Created by CreeperFace on 9. 12. 2016.
 */
public class SetSpawnCommand extends CommandBase {

    public SetSpawnCommand(EssentialsAPI api) {
        super("setspawn", api);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }

        if (!this.testIngame(sender)) {
            sender.sendMessage(TextFormat.RED+lang.translateString("commands.generic.ingame"));
            return false;
        }

        if (args.length != 0) {
            this.sendUsage(sender);
            return false;
        }

        Player p = (Player) sender;
        getAPI().getServer().setDefaultLevel(p.getLevel());
        p.getLevel().setSpawnLocation(p);

        p.sendMessage(TextFormat.YELLOW+lang.translateString("commands.setspawn.success"));
        getAPI().getLogger().info(TextFormat.YELLOW +"Server's spawn point set to " + TextFormat.AQUA + p.getLevel().getName() + TextFormat.YELLOW + " by " + TextFormat.GREEN + p.getName());
        return true;
    }
}
