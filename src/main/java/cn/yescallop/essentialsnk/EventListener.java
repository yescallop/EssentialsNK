package cn.yescallop.essentialsnk;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.yescallop.essentialsnk.lang.BaseLang;

public class EventListener implements Listener {
    
    EssentialsNK plugin;
    BaseLang lang;
    
    public EventListener(EssentialsNK plugin) {
        this.plugin = plugin;
        lang = plugin.getLanguage();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        plugin.setPlayerLastLocation(event.getPlayer(), event.getFrom());
    }
}