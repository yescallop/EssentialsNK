package cn.yescallop.essentialsnk;

import cn.nukkit.plugin.PluginBase;
import cn.yescallop.essentialsnk.command.CommandManager;
import cn.yescallop.essentialsnk.lang.BaseLang;

public class EssentialsNK extends PluginBase {

    private BaseLang lang;
    private static EssentialsAPI api;

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();
        this.lang = new BaseLang(this.getServer().getLanguage().getLang());
        api = new EssentialsAPI(this);
        CommandManager.registerAll(api);
        this.getServer().getPluginManager().registerEvents(new EventListener(api), this);
        this.getLogger().info(lang.translateString("essentialsnk.loaded"));
    }

    public BaseLang getLanguage() {
        return lang;
    }

    public static EssentialsAPI getAPI() {
        return api;
    }
}