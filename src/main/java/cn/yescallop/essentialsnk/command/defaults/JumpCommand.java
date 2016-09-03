package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.command.CommandSender;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.command.CommandBase;

public class JumpCommand extends CommandBase {

    public JumpCommand(EssentialsNK plugin) {
        super("jump", plugin);
        this.setAliases(new String[]{"j", "jumpto"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }
        if (args.length != 0) {
            this.sendUsage(sender);
            return false;
        }
        Player player = (Player) sender;
        Block block = player.getTargetBlock(120, EssentialsNK.NON_SOLID_BLOCKS);
        if (block == null) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.jump.unreachable"));
            return false;
        }
        if (!player.getLevel().getBlock(block.add(0, 2)).isSolid()) {
            player.teleport(block.add(0, 1));
            return false;
        }
        int side;
        switch (player.getDirection()) {
            case 0:
                side = Vector3.SIDE_SOUTH;
                break;
            case 1:
                side = Vector3.SIDE_WEST;
                break;
            case 2:
                side = Vector3.SIDE_NORTH;
                break;
            case 3:
                side = Vector3.SIDE_EAST;
                break;
            default:
                return false;
        }
        block = block.getSide(side);
        if (!player.getLevel().getBlock(block.add(0, 2)).isSolid()) {
            player.teleport(block.add(0, 1));
        }
        return true;
    }
}
