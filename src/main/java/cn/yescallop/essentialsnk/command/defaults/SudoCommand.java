package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

import java.util.Arrays;

public class SudoCommand extends CommandBase {

    public SudoCommand(EssentialsAPI api) {
        super("sudo", api);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length < 2) {
            this.sendUsage(sender);
            return false;
        }
        Player player = api.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
            return false;
        }
        String msg = api.implode(" ", Arrays.copyOfRange(args, 1, args.length));
        if (msg.length() > 1 && msg.substring(0, 2).equals("c:")) {
            sender.sendMessage(lang.translateString("commands.sudo.message", player.getDisplayName()));
            PlayerChatEvent event = new PlayerChatEvent(player, msg.substring(2));
            api.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                api.getServer().broadcastMessage(api.getServer().getLanguage().translateString(event.getFormat(), new String[]{event.getPlayer().getDisplayName(), event.getMessage()}), event.getRecipients());
            }
        } else {
            sender.sendMessage(lang.translateString("commands.sudo.command", player.getDisplayName()));
            api.getServer().dispatchCommand(player, msg);
        }
        return true;
    }
}
