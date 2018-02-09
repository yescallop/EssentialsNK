package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.Language;
import cn.yescallop.essentialsnk.command.CommandBase;
import cn.yescallop.essentialsnk.util.duration.LMLDurationParser;

import java.time.Duration;
import java.util.Arrays;
import java.util.regex.Pattern;

public class MuteCommand extends CommandBase {

    private static Pattern p = Pattern.compile("^[0-9]+$");

    public MuteCommand(EssentialsAPI api) {
        super("mute", api);
        this.setAliases(new String[]{"silence"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length < 1) {
            this.sendUsage(sender);
            return false;
        }
        Player player = api.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.player.notfound", args[0]));
            return false;
        }
        if (sender == player) {
            sender.sendMessage(TextFormat.RED + Language.translate("commands.mute.self"));
            return false;
        }
        if (args.length > 1) {
            Duration duration;
            try {
                /*  /mute lmlstarqaq 0 0 1   */
                if (args.length == 5 && isPositiveInteger(args[1]) && isPositiveInteger(args[2]) && isPositiveInteger(args[3]) && isPositiveInteger(args[4]))
                    duration = Duration.ZERO
                            .plusDays(Integer.parseInt(args[1]))
                            .plusHours(Integer.parseInt(args[2]))
                            .plusMinutes(Integer.parseInt(args[3]))
                            .plusSeconds(Integer.parseInt(args[4]));
                else {
                    String arg = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();
                    duration = LMLDurationParser.parse(arg);
                }
            } catch (Exception e) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.number.invalid"));
                return false;
            }
            if (duration == null) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.generic.time.invalidtext"));
                return false;
            }
            if (duration.isZero()) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.mute.zero"));
                return false;
            }
            String message = api.getDurationString(duration);
            if (!api.mute(player, duration)) {
                sender.sendMessage(TextFormat.RED + Language.translate("commands.mute.range"));
                return false;
            }
            sender.sendMessage(Language.translate("commands.mute.success", player.getDisplayName(), message));
            player.sendMessage(Language.translate("commands.mute.other", message));
        } else { // args.length == 1
            api.unmute(player);
            sender.sendMessage(Language.translate("commands.mute.unmute.success", player.getDisplayName()));
            player.sendMessage(Language.translate("commands.mute.unmute.other"));
        }
        return true;
    }

    private boolean isPositiveInteger(String a) {
        return p.matcher(a).find();
    }
}
