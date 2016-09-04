package cn.yescallop.essentialsnk.command;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
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
}