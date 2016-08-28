package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class HealCommand extends CommandBase {

    public HealCommand(EssentialsNK plugin) {
        super("heal", plugin);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        Player player;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextFormat.RED + lang.translateString("generic.ingame"));
                return true;
            }
            player = (Player) sender;
        } else if (args.length == 1) {
            if (!sender.hasPermission("essentialsnk.heal.other")) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return true;
            }
            player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(TextFormat.RED + lang.translateString("generic.player.notFound", args[0]));
                return true;
            }
        } else {
            return false;
        }
        player.heal(player.getMaxHealth() - player.getHealth());
        player.getLevel().addParticle(new HeartParticle(player.add(0, 2), 4));
        player.sendMessage(lang.translateString("heal.success"));
        if (sender != player) {
            sender.sendMessage(lang.translateString("heal.success.other"));
        }
        return true;
    }
}
