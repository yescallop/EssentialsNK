package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
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
            if (!this.testIngame(sender)) {
                return false;
            }
            player = (Player) sender;
        } else {
            player = api.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
                return false;
            }
        }
        Position pos = args.length == 1 ? player : player.getTargetBlock(120);
        if (pos == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.lightning.unreachable"));
            return false;
        }
        api.strikeLighting(pos);
        sender.sendMessage(Language.translate("commands.lightning.success"));
        return true;
    }
}
