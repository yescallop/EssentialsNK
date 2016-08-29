package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class KickAllCommand extends CommandBase {

    public KickAllCommand(EssentialsNK plugin) {
        super("kickall", plugin);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        int count = plugin.getServer().getOnlinePlayers().size();
        if (count == 0 || (sender instanceof Player && count == 1)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.kickall.noplayer"));
            return true;
        }
        String reason = plugin.parseMessage(args);
        for (Player player : plugin.getServer().getOnlinePlayers().values()) {
            if (player != sender) {
                player.kick(reason);
            }
        }
        sender.sendMessage(lang.translateString("commands.kickall.success"));
        return true;
    }
}
