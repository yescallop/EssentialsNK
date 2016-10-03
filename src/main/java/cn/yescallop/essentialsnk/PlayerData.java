package cn.yescallop.essentialsnk;

import cn.nukkit.utils.ConfigSection;

public class PlayerData {

    public boolean godMode = false;

    public final String player;

    public PlayerData(String p) {
        this.player = p.toLowerCase();
    }

    public ConfigSection encode() {
        ConfigSection data = new ConfigSection();
        data.put("godMode", godMode);

        return data;
    }

    public void decode(ConfigSection data) {
        godMode = data.getBoolean("godMode");
    }
}
