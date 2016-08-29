package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class BreakCommand extends CommandBase {

    public BreakCommand(EssentialsNK plugin) {
        super("break", plugin);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return true;
        }
        if (args.length != 0) {
            return false;
        }
        Player player = (Player) sender;
        Block block = player.getTargetBlock(120);
        if (block.getId() == Block.AIR) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.break.unreachable"));
            return true;
        }
        player.getLevel().setBlock(block, new BlockAir(), true, true);
        return true;
    }
}
