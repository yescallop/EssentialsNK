package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

import javax.xml.datatype.DatatypeFactory;
import java.time.Duration;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MuteCommand extends CommandBase {

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
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
            return false;
        }
        if (sender == player) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.mute.self"));
            return false;
        }
        if (args.length >= 2) {
            Duration duration;
            try {
                /*  /mute lmlstarqaq 0 0 1   */
                if (args.length == 4 && isPositiveInteger(args[1]) && isPositiveInteger(args[2]) && isPositiveInteger(args[3]))
                    duration = Duration.ZERO
                            .plusDays(Integer.parseInt(args[1]))
                            .plusHours(Integer.parseInt(args[2]))
                            .plusMinutes(Integer.parseInt(args[3]));
                /*
                    /mute lmlstarqaq 04:00:00
                    /mute lmlstarqaq 4 seconds
                    /mute lmlstarqaq one day
                    /mute lmlstarqaq three hours and a half
                    /mute lmlstarqaq 六个半小时
                    /mute lmlstarqaq восемь минут

                    Thanks to powerful java!
                */
                else {
                    String arg = "";
                    for (int i = 1/* player name */; i < args.length; i++) arg += args[i];
                    duration = Duration.ofSeconds(DatatypeFactory.newInstance().newDuration(arg).getTimeInMillis(Calendar.getInstance()));
                }
            } catch (Exception e) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.number.invalid"));
                return false;
            }
            if (duration.isZero()) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.mute.zero"));
                return false;
            }
            String message = api.getDurationString(duration);
            if (!api.mute(player, duration)) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.mute.range"));
                return false;
            }
            sender.sendMessage(lang.translateString("commands.mute.success", player.getDisplayName(), message));
            player.sendMessage(lang.translateString("commands.mute.other", message));
        }
        else { // args.length == 1
            api.unmute(player);
            sender.sendMessage(lang.translateString("commands.mute.unmute.success", player.getDisplayName()));
            player.sendMessage(lang.translateString("commands.mute.unmute.other"));
        }
        return true;
    }

    private static Pattern p = Pattern.compile("^[0-9]+$");
    private boolean isPositiveInteger(String a) {
        return p.matcher(a).find();
    }
}
