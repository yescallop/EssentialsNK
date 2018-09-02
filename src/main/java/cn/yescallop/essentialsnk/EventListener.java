package cn.yescallop.essentialsnk;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import cn.nukkit.level.Location;

public class EventListener implements Listener {

    EssentialsAPI api;

    public EventListener(EssentialsAPI api) {
        this.api = api;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        api.setLastLocation(event.getPlayer(), event.getFrom());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Block bed = event.getBed();
        api.setHome(event.getPlayer(), "bed", Location.fromObject(bed, bed.level, 0, 0));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player p : api.getServer().getOnlinePlayers().values()) {
            if (api.isVanished(p)) {
                player.hidePlayer(p);
            }
            if (api.isVanished(player)) {
                p.hidePlayer(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerJoinEvent event){
        this.api.updatePrefixSuffix(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        api.removeTPRequest(player);
        player.setDisplayName(player.getName()); //In attempt to not make it so their username when they leave. Doesn't work
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (api.isMuted(player)) {
            event.setCancelled();
            player.sendMessage(Language.translate("commands.generic.muted", api.getUnmuteTimeMessage(player)));
        }
        //TODO:Make a config that changes chat format
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (api.isMuted(player)) {
            event.setCancelled();
            player.sendMessage(Language.translate("commands.generic.muted", api.getUnmuteTimeMessage(player)));
        }
    }
}