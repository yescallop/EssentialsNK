package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;

public class BackCommand extends CommandBase {

    public BackCommand(EssentialsAPI api) {
        super("back", api);
        this.setAliases(new String[]{"return"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        if (args.length != 0) {
            this.sendUsage(sender);
            return false;
        }
        Player player = (Player) sender;
        Location pos = api.getLastLocation(player);
        if (pos == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.back.notavalible"));
            return false;
        }
        player.teleport(pos);
        sender.sendMessage(Language.translate("commands.generic.teleporting"));
        return true;
    }
}
