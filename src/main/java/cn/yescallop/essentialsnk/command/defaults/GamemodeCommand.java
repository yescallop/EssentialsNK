package cn.yescallop.essentialsnk.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.command.CommandBase;

public class GamemodeCommand extends CommandBase {

    public GamemodeCommand(EssentialsAPI api) {
        super("gamemode", api);
        this.setAliases(new String[]{"gm", "gma", "gmc", "gms", "gmt", "adventure", "creative", "survival", "spectator", "viewer"});
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        Player player;
        int gamemode;
        if (label.toLowerCase().equals("gamemode") || label.toLowerCase().equals("gm")) {
            if (args.length == 0 || args.length > 2) {
                this.sendUsage(sender, label);
                return false;
            }
            if (args.length == 1) {
                if (!this.testIngame(sender)) {
                    return false;
                }
                player = (Player) sender;
            } else {
                if (!sender.hasPermission("essentialsnk.gamemode.other")) {
                    this.sendPermissionMessage(sender);
                    return false;
                }
                player = api.getServer().getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[1]));
                    return false;
                }
            }
            gamemode = Server.getGamemodeFromString(args[0]);
            if (gamemode == -1) {
                sender.sendMessage(TextFormat.RED + lang.translateString("commands.gamemode.invalid", args[0]));
                return false;
            }
        } else {
            if (args.length > 1) {
                this.sendUsage(sender, label);
                return false;
            }
            if (args.length == 0) {
                if (!this.testIngame(sender)) {
                    return false;
                }
                player = (Player) sender;
            } else {
                player = api.getServer().getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.player.notfound", args[0]));
                    return false;
                }
            }
            switch (label.toLowerCase()) {
                case "survival":
                case "gms":
                    gamemode = Player.SURVIVAL;
                    break;
                case "creative":
                case "gmc":
                    gamemode = Player.CREATIVE;
                    break;
                case "adventure":
                case "gma":
                    gamemode = Player.ADVENTURE;
                    break;
                case "spectator":
                case "viewer":
                case "gmt":
                    gamemode = Player.SPECTATOR;
                    break;
                default:
                    return false;
            }
        }
        player.setGamemode(gamemode);
        String gamemodeStr = Server.getGamemodeString(gamemode);
        player.sendMessage(lang.translateString("commands.gamemode.success", gamemodeStr));
        if (sender != player) {
            sender.sendMessage(lang.translateString("commands.gamemode.success.other", player.getDisplayName(), gamemodeStr));
        }
        return true;
    }

    private void sendUsage(CommandSender sender, String label) {
        String usage;
        if (label.toLowerCase().equals("gamemode") || label.toLowerCase().equals("gm")) {
            usage = lang.translateString("commands.gamemode.usage1", new String[]{label.toLowerCase()});
        } else {
            usage = lang.translateString("commands.gamemode.usage2", new String[]{label.toLowerCase()});
        }
        sender.sendMessage(new TranslationContainer("commands.generic.usage", usage));
    }
}
