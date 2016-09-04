package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class LightningCommand extends CommandBase {

    public LightningCommand(EssentialsAPI api) {
        super("lightning", api);
        this.setAliases(new String[]{"strike", "smite", "thor", "shock"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length > 1) {
            this.sendUsage(sender);
            return false;
        }
        Player player;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
                return false;
            }
            player = (Player) sender;
        } else {
            player = api.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        Position pos = args.length == 1 ? player : player.getTargetBlock(120);
        if (pos == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.lightning.unreachable"));
            return false;
        }
        api.strikeLighting(pos);
        sender.sendMessage(lang.translateString("commands.lightning.success"));
        return true;
    }
}
