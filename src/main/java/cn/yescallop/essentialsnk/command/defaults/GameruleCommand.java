package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class GameruleCommand extends CommandBase {

    public GameruleCommand(EssentialsAPI api) {
        super("gamerule", api);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (args.length > 2) {
            this.sendUsage(sender);
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage("doFileTick, doMobLoot, doTileDroPS, keepInventory, naturalRegeneration");
            return true;
        }
        if (!this.testIngame(sender)) {
            return false;
        }
        Level level = ((Player) sender).getLevel();
        if (args.length == 1) {
            switch (args[0]) {
                case "doFireTick":
                case "doMobLoot":
                case "doTileDroPS":
                case "keepInventory":
                case "naturalRegeneration":
                    sender.sendMessage(args[0] + " = " + api.getGamerule(level, args[0]));
                    break;
                default:
                    sender.sendMessage(lang.translateString("commands.gamerule.unknown", args[0]));
                    return false;
            }
            return true;
        }
        Object value;
        switch (args[0]) {
            case "doFireTick":
            case "doMobLoot":
            case "doTileDroPS":
            case "keepInventory":
            case "naturalRegeneration":
                switch (args[1]) {
                    case "true":
                        value = true;
                        break;
                    case "false":
                        value = false;
                        break;
                    default:
                        sender.sendMessage(lang.translateString("commands.generic.invalidboolean", args[1]));
                        return true;
                }
                break;
            default:
                sender.sendMessage(lang.translateString("commands.gamerule.unknown", args[0]));
                return true;
        }
        api.setGamerule(level, args[0], value);
        sender.sendMessage(lang.translateString("commands.gamerule.success", new String[]{args[0], value.toString()}));
        return true;
    }
}
