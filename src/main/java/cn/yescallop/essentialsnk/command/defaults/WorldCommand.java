package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class WorldCommand extends CommandBase {

    public WorldCommand(EssentialsAPI api) {
        super("world", api);
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
        if (!api.getServer().isLevelGenerated(args[0])) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.world.notfound", args[0]));
            return false;
        } else if (!api.getServer().isLevelLoaded(args[0])) {
            sender.sendMessage(Language.translate("commands.world.loading"));
            if (!api.getServer().loadLevel(args[0])) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.world.unloadable"));
                return false;
            }
        }
        ((Player) sender).teleport(api.getServer().getLevelByName(args[0]).getSpawnLocation());
        sender.sendMessage(Language.translate("commands.generic.teleporting"));
        return true;
    }
}
