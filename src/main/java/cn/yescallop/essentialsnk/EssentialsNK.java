package cn.yescallop.essentialsnk;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Position;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
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
    private Map<Integer, TPRequest> tpRequests = new HashMap<>();

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
    
    public String parseMessage(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (builder.length() != 0) {
                builder.append(" ");
            }
            builder.append(args[i]);
        }
        return builder.toString();
    }
    
    public void setPlayerLastLocation(Player player, Location pos) {
        this.playerLastLocation.put(player, pos);
    }
    
    public Location getPlayerLastLocation(Player player) {
        return this.playerLastLocation.get(player);
    }
    
    public boolean switchAllowFlight(Player player) {
        boolean b;
        player.setAllowFlight(b = !player.getAllowFlight());
        return b;
    }
    
    public boolean switchVanish(Player player) {
        boolean b;
        player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_INVISIBLE, b = !player.getDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_INVISIBLE));
        return b;
    }
    
    public boolean isRepairable(Item item) {
        return item instanceof ItemTool || item instanceof ItemArmor;
    }
    
    public void strikeLighting(Position pos) {
        FullChunk chunk = pos.getLevel().getChunk((int) pos.getX() >> 4, (int) pos.getZ() >> 4);
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", pos.getX()))
                        .add(new DoubleTag("", pos.getY()))
                        .add(new DoubleTag("", pos.getZ())))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)));
        EntityLightning lightning = new EntityLightning(chunk, nbt);
        lightning.spawnToAll();
    }

    private int getHashCode(Player from, Player to, boolean isTo) {
        return from.hashCode() + to.hashCode() + Boolean.hashCode(isTo);
    }
    
    public void requestTP(Player from, Player to, boolean isTo) {
        this.tpRequests.put(this.getHashCode(from, to, isTo), new TPRequest(System.currentTimeMillis(), from, to, isTo));
    }
    
    public TPRequest getLatestTPRequestTo(Player player) {
        TPRequest latest = null;
        for (TPRequest request : this.tpRequests.values()) {
            if (request.getTo() == player && (latest == null || request.getStartTime() > latest.getStartTime())) {
                latest = request;
            }
        }
        return latest;
    }
    
    public TPRequest getTPRequestBetween(Player from, Player to) {
        int key;
        if (this.tpRequests.containsKey(key = this.getHashCode(from, to, true)) || this.tpRequests.containsKey(key = this.getHashCode(from, to, false))) {
            return this.tpRequests.get(key);
        }
        return null;
    }
    
    public boolean hasTPRequestBetween(Player from, Player to) {
        return this.tpRequests.containsKey(this.getHashCode(from, to, true)) || this.tpRequests.containsKey(this.getHashCode(from, to, false));
    }
    
    public void removeTPRequestBetween(Player from, Player to) {
        this.tpRequests.remove(this.getHashCode(from, to, true));
        this.tpRequests.remove(this.getHashCode(from, to, false));
    }
}