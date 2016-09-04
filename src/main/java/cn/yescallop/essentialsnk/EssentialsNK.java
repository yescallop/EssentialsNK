package cn.yescallop.essentialsnk;

import cn.nukkit.plugin.PluginBase;
import cn.yescallop.essentialsnk.command.CommandManager;
import cn.yescallop.essentialsnk.lang.BaseLang;

public class EssentialsNK extends PluginBase {

    private BaseLang lang;
    private EssentialsAPI api;

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();
        this.lang = new BaseLang(this.getServer().getLanguage().getLang());
        this.api = new EssentialsAPI(this);
        CommandManager.registerAll(this.getServer().getCommandMap(), this.api);
        this.getServer().getPluginManager().registerEvents(new EventListener(this.api), this);
        this.getLogger().info(lang.translateString("essentialsnk.loaded"));
    }
    
    public BaseLang getLanguage() {
        return lang;
    }
    
    public EssentialsAPI getAPI() {
        return api;
    }
}