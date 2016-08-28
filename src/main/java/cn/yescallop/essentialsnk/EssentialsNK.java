package cn.yescallop.essentialsnk;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.yescallop.essentialsnk.command.CommandManager;
import cn.yescallop.essentialsnk.lang.BaseLang;

import java.util.HashMap;
import java.util.Map;

public class EssentialsNK extends PluginBase {

    private BaseLang lang;
    private Map<Player, Location> playerLastLocation = new HashMap<>();

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();
        lang = new BaseLang(this.getServer().getLanguage().getLang());
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        CommandManager.registerAll(this);
        this.getLogger().info(lang.translateString("essentialsnk.loaded"));
    }
    
    public BaseLang getLanguage() {
        return lang;
    }
    
    public void setPlayerLastLocation(Player player) {
        this.setPlayerLastLocation(player, player.getLocation());
    }
    
    public void setPlayerLastLocation(Player player, Location pos) {
        this.playerLastLocation.put(player, pos);
    }
    
    public Location getPlayerLastLocation(Player player) {
        return this.playerLastLocation.get(player);
    }
}