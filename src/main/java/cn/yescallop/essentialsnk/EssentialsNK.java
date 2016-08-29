package cn.yescallop.essentialsnk;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.yescallop.essentialsnk.command.CommandManager;
import cn.yescallop.essentialsnk.lang.BaseLang;

import java.util.HashMap;
import java.util.Map;

public class EssentialsNK extends PluginBase {

    private BaseLang lang;
    private Map<Player, Location> playerLastLocation = new HashMap<>();
    public static final Integer[] NON_SOLID_BLOCKS = new Integer[]{Block.AIR, Block.SAPLING, Block.WATER, Block.STILL_WATER, Block.LAVA, Block.STILL_LAVA, Block.COBWEB, Block.TALL_GRASS, Block.BUSH, Block.DANDELION,
        Block.POPPY, Block.BROWN_MUSHROOM, Block.RED_MUSHROOM, Block.TORCH, Block.FIRE, Block.WHEAT_BLOCK, Block.SIGN_POST, Block.WALL_SIGN, Block.SUGARCANE_BLOCK,
        Block.PUMPKIN_STEM, Block.MELON_STEM, Block.VINE, Block.CARROT_BLOCK, Block.POTATO_BLOCK, Block.DOUBLE_PLANT};

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
    
    public boolean switchAllowFlight(Player player) {
        boolean allow;
        player.setAllowFlight(allow = !player.getAllowFlight());
        return allow;
    }
    
    public boolean isRepairable(Item item) {
        return item instanceof ItemTool || item instanceof ItemArmor;
    }
}