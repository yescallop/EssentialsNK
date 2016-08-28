package cn.yescallop.essentialsnk.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.yescallop.essentialsnk.EssentialsNK;
import cn.yescallop.essentialsnk.lang.BaseLang;

public abstract class CommandBase extends Command implements PluginIdentifiableCommand {

    protected EssentialsNK plugin;
    protected BaseLang lang;

    public CommandBase(String name, EssentialsNK plugin) {
        super(name);
        this.lang = plugin.getLanguage();
        this.description = lang.translateString(name + ".description");
        String usageMessage = lang.translateString(name + ".usage");
        this.usageMessage = usageMessage.equals(name + ".usage") ? "/" + name : usageMessage;
        this.setPermission("essentialsnk." + name);
        this.plugin = plugin;
    }

    @Override
    public EssentialsNK getPlugin() {
        return plugin;
    }
}