package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class WorldCommand extends CommandBase {

    public WorldCommand(EssentialsNK plugin) {
        super("world", plugin);
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
        if (!plugin.getServer().isLevelGenerated(args[0])) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.world.notfound"));
            return false;
        } else if (!plugin.getServer().isLevelLoaded(args[0])) {
            sender.sendMessage(lang.translateString("commands.world.loading"));
            if (!plugin.getServer().loadLevel(args[0])) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.world.unloadable"));
                return false;
            }
        }
        ((Player) sender).teleport(plugin.getServer().getLevelByName(args[0]).getSpawnLocation());
        sender.sendMessage(lang.translateString("commands.world.success"));
        return true;
    }
}
