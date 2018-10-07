package cn.yescallop.essentialsnk.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.permission.Permissible;
import cn.nukkit.utils.TextFormat;
import cn.yescallop.essentialsnk.EssentialsAPI;
import cn.yescallop.essentialsnk.lang.BaseLang;

public abstract class CommandBase extends Command {

    protected EssentialsAPI api;
    protected BaseLang lang;
    protected Server server;

    public CommandBase(String name, EssentialsAPI api) {
        super(name);
        this.lang = api.getLanguage();
        this.description = lang.translateString("commands." + name + ".description");
        String usageMessage = lang.translateString("commands." + name + ".usage");
        this.usageMessage = usageMessage.equals("commands." + name + ".usage") ? "/" + name : usageMessage;
        this.setPermission("essentialsnk." + name);
        this.api = api;
    }

    protected EssentialsAPI getAPI() {
        return api;
    }

    protected void sendUsage(CommandSender sender) {
        sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
    }

    protected boolean testIngame(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + lang.translateString("commands.generic.ingame"));
            return false;
        }
        return true;
    }

    protected void sendPermissionMessage(CommandSender sender) {
        sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
    }

    protected int getPermissionValue(Permissible holder, String basePerm) {
        if (holder.hasPermission(basePerm))
            return -1;

        int val = 0;

        for (String perm : holder.getEffectivePermissions().keySet().stream()
                .filter(name -> name.startsWith(basePerm + "."))
                .map(name -> name.substring(17))
                .toArray(String[]::new)) {

            int value = -1;

            try {
                value = Integer.parseInt(perm);
            } catch (NumberFormatException e) {
                //ignore
            }

            if (value > 0) {
                val = value;
            }
        }

        if (val <= 0) {
            val = 10;
        }

        return val;
    }
}