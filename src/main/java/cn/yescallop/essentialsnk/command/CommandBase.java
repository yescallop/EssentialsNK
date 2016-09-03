package cn.yescallop.essentialsnk.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.lang.BaseLang;

public abstract class CommandBase extends Command implements PluginIdentifiableCommand {

    protected EssentialsNK plugin;
    protected BaseLang lang;

    public CommandBase(String name, EssentialsNK plugin) {
        super(name);
        this.lang = plugin.getLanguage();
        this.description = lang.translateString("commands." + name + ".description");
        String usageMessage = lang.translateString("commands." + name + ".usage");
        this.usageMessage = usageMessage.equals("commands." + name + ".usage") ? "/" + name : usageMessage;
        this.setPermission("essentialsnk." + name);
        this.plugin = plugin;
    }

    @Override
    protected EssentialsNK getPlugin() {
        return plugin;
    }
    
    protected void sendUsage(CommandSender sender) {
        sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
    }
}