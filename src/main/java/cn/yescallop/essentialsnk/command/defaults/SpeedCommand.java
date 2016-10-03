package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class SpeedCommand extends CommandBase {

    public SpeedCommand(EssentialsAPI api) {
        super("speed", api);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }

        if (args.length < 1 || args.length > 2) {
            this.sendUsage(sender);
            return false;
        }

        int speed;

        try {
            speed = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(lang.translateString("commands.generic.number.invalidinteger"));
            return false;
        }

        Player player;

        if (args.length == 2) {
            player = getAPI().getServer().getPlayer(args[1]);
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        } else {
            player = (Player) sender;
        }

        if (player == null || !player.isOnline()) {
            sender.sendMessage(lang.translateString("commands.generic.player.notfound"));
            return false;
        }

        player.setMovementSpeed(speed);

        sender.sendMessage(lang.translateString("commands.speed.success", player.getName(), String.valueOf(speed)));
        return true;
    }
}
