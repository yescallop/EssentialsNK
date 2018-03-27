package cn.yescallop.essentialsnk;

import cn.nukkit.Player;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.user.UserDataRecalculateEvent;

public class LPListener {
    private final EssentialsAPI api;
    private final LuckPermsApi LPapi;

    public LPListener(EssentialsAPI api, LuckPermsApi LPapi) {
        this.api = api;
        this.LPapi = LPapi;

        // get the LuckPerms event bus
        EventBus eventBus = LPapi.getEventBus();

        // subscribe to an event using a method reference
        eventBus.subscribe(UserDataRecalculateEvent.class, this::onUserDataRecalculate);
    }

    private void onUserDataRecalculate(UserDataRecalculateEvent event) {
        // as we want to access the Nukkit API, we need to use the scheduler to jump back onto the main thread.
        this.api.getServer().getScheduler().scheduleTask(this.api.getPlugin(), () -> {
            if (!this.api.getServer().getOnlinePlayers().containsKey(event.getUser().getUuid())) //Checks if user is online
                return;
            Player player = this.api.getServer().getPlayer(event.getUser().getName());
            if (player != null) {
                Contexts contexts = this.LPapi.getContextManager().getApplicableContexts(player);
                MetaData metaData = event.getData().getMetaData(contexts);
                this.api.setPrefixSuffixData(player,metaData.getPrefix(),metaData.getSuffix());
            }
        });
    }
}
