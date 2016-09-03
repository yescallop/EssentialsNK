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
import cn.nukkit.utils.Config;
import cn.yescallop.essentialsnk.command.CommandManager;
import cn.yescallop.essentialsnk.lang.BaseLang;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EssentialsNK extends PluginBase {

    private BaseLang lang;
    private Map<Player, Location> playerLastLocation = new HashMap<>();
    public static final Integer[] NON_SOLID_BLOCKS = new Integer[]{Block.AIR, Block.SAPLING, Block.WATER, Block.STILL_WATER, Block.LAVA, Block.STILL_LAVA, Block.COBWEB, Block.TALL_GRASS, Block.BUSH, Block.DANDELION,
        Block.POPPY, Block.BROWN_MUSHROOM, Block.RED_MUSHROOM, Block.TORCH, Block.FIRE, Block.WHEAT_BLOCK, Block.SIGN_POST, Block.WALL_SIGN, Block.SUGARCANE_BLOCK,
        Block.PUMPKIN_STEM, Block.MELON_STEM, Block.VINE, Block.CARROT_BLOCK, Block.POTATO_BLOCK, Block.DOUBLE_PLANT};
    private Map<Integer, TPRequest> tpRequests = new HashMap<>();
    private Config homeConfig;
    private Config warpConfig;

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();
        this.homeConfig = new Config(new File(this.getDataFolder(), "home.yml"), Config.YAML);
        this.warpConfig = new Config(new File(this.getDataFolder(), "warp.yml"), Config.YAML);
        this.lang = new BaseLang(this.getServer().getLanguage().getLang());
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        CommandManager.registerAll(this);
        this.getLogger().info(lang.translateString("essentialsnk.loaded"));
    }
    
    public BaseLang getLanguage() {
        return lang;
    }
    
    public String implode(String[] args, String glue) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (builder.length() != 0) {
                builder.append(glue);
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
    
    public boolean setHome(Player player, String name, Location pos) {
        this.homeConfig.reload();
        Map<String, Object[]> map = this.homeConfig.get(player.getName().toLowerCase(), new HashMap<>());
        boolean replaced = map.containsKey(name);
        Object[] home = new Object[]{pos.level.getName(), pos.x, pos.y, pos.z, pos.yaw, pos.pitch};
        map.put(name, home);
        this.homeConfig.set(player.getName().toLowerCase(), map);
        this.homeConfig.save();
        return replaced;
    }
    
    public Location getHome(Player player, String name) {
        this.homeConfig.reload();
        Map<String, ArrayList<Object>> map = (Map<String, ArrayList<Object>>) this.homeConfig.get(player.getName().toLowerCase());
        if (map == null) {
            return null;
        }
        List<Object> home = map.get(name);
        if (home == null || home.size() != 6) {
            return null;
        }
        return new Location((double) home.get(1), (double) home.get(2), (double) home.get(3), (double) home.get(4), (double) home.get(5), this.getServer().getLevelByName((String) home.get(0)));
    }
    
    public void removeHome(Player player, String name) {
        this.homeConfig.reload();
        Map<String, Object> map = (Map<String, Object>) this.homeConfig.get(player.getName().toLowerCase());
        if (map == null) {
            return;
        }
        map.remove(name);
        this.homeConfig.set(player.getName().toLowerCase(), map);
        this.homeConfig.save();
    }
    
    public String[] getHomesList(Player player) {
        this.homeConfig.reload();
        Map<String, Object> map = (Map<String, Object>) this.homeConfig.get(player.getName().toLowerCase());
        if (map == null) {
            return new String[]{};
        }
        String[] list = map.keySet().stream().toArray(String[]::new);
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }
    
    public boolean isHomeExists(Player player, String name) {
        this.homeConfig.reload();
        Map<String, Object> map = (Map<String, Object>) this.homeConfig.get(player.getName().toLowerCase());
        if (map == null) {
            return false;
        }
        return map.containsKey(name);
    }
    
    public boolean setWarp(String name, Location pos) {
        this.warpConfig.reload();
        boolean replaced = warpConfig.exists(name);
        Object[] home = new Object[]{pos.level.getName(), pos.x, pos.y, pos.z, pos.yaw, pos.pitch};
        this.warpConfig.set(name, home);
        this.warpConfig.save();
        return replaced;
    }
    
    public Location getWarp(String name) {
        this.warpConfig.reload();
        List<Object> warp = this.warpConfig.getList(name);
        if (warp == null || warp.size() != 6) {
            return null;
        }
        return new Location((double) warp.get(1), (double) warp.get(2), (double) warp.get(3), (double) warp.get(4), (double) warp.get(5), this.getServer().getLevelByName((String) warp.get(0)));
    }
    
    public void removeWarp(String name) {
        this.warpConfig.reload();
        this.warpConfig.remove(name);
        this.warpConfig.save();
    }
    
    public String[] getWarpsList() {
        this.warpConfig.reload();
        Map<String, Object> map = this.warpConfig.getAll();
        String[] list = map.keySet().stream().toArray(String[]::new);
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }
    
    public boolean isWarpExists(String name) {
        this.warpConfig.reload();
        return this.warpConfig.exists(name);
    }
}