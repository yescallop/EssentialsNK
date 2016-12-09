package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.potion.Effect;
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
        if (args.length == 0 || args.length > 2) {
            this.sendUsage(sender);
            return false;
        }
        int speed;
        try {
            speed = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.number.invalidinteger", args[0]));
            return false;
        }
        Player player;
        if (args.length == 2) {
            if (!sender.hasPermission("essentialsnk.speed.others")) {
                this.sendPermissionMessage(sender);
                return false;
            }
            player = api.getServer().getPlayer(args[1]);
        } else if (!this.testIngame(sender)) {
            return false;
        } else {
            player = (Player) sender;
        }
        if (player == null) {
            sender.sendMessage(lang.translateString("commands.generic.player.notfound", args[0]));
            return false;
        }
        player.removeEffect(Effect.SPEED);
        if (speed != 0) {
            player.addEffect(
                    Effect.getEffect(Effect.SPEED)
                            .setAmplifier(speed)
                            .setDuration(Integer.MAX_VALUE)
            );
        }
        if (sender == player) {
            sender.sendMessage(lang.translateString("commands.speed.success", speed));
        } else {
            sender.sendMessage(lang.translateString("commands.speed.success.other", player.getDisplayName(), speed));
        }
        return true;
    }
}