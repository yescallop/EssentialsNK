package cn.yescallop.essentialsnk;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Config;
import cn.yescallop.essentialsnk.lang.BaseLang;

import java.io.File;
import java.util.*;

public class EssentialsAPI {

    public static final Integer[] NON_SOLID_BLOCKS = new Integer[]{Block.AIR, Block.SAPLING, Block.WATER, Block.STILL_WATER, Block.LAVA, Block.STILL_LAVA, Block.COBWEB, Block.TALL_GRASS, Block.BUSH, Block.DANDELION,
            Block.POPPY, Block.BROWN_MUSHROOM, Block.RED_MUSHROOM, Block.TORCH, Block.FIRE, Block.WHEAT_BLOCK, Block.SIGN_POST, Block.WALL_SIGN, Block.SUGARCANE_BLOCK,
            Block.PUMPKIN_STEM, Block.MELON_STEM, Block.VINE, Block.CARROT_BLOCK, Block.POTATO_BLOCK, Block.DOUBLE_PLANT};
    private static EssentialsAPI instance = null;
    private EssentialsNK plugin;
    private BaseLang lang;
    private Map<Player, Location> playerLastLocation = new HashMap<>();
    private Map<Integer, TPRequest> tpRequests = new HashMap<>();
    private List<Player> vanishedPlayers = new ArrayList<>();
    public Vector3 temporalVector = new Vector3();
    private Config homeConfig;
    private Config warpConfig;
    private Config muteConfig;

    public EssentialsAPI(EssentialsNK plugin) {
        instance = this;
        this.plugin = plugin;
        this.lang = plugin.getLanguage();
        this.homeConfig = new Config(new File(plugin.getDataFolder(), "home.yml"), Config.YAML);
        this.warpConfig = new Config(new File(plugin.getDataFolder(), "warp.yml"), Config.YAML);
        this.muteConfig = new Config(new File(plugin.getDataFolder(), "mute.yml"), Config.YAML);
    }

    public static EssentialsAPI getInstance() {
        return instance;
    }

    public Server getServer() {
        return plugin.getServer();
    }

    public BaseLang getLanguage() {
        return lang;
    }

    public String implode(String glue, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (builder.length() != 0) {
                builder.append(glue);
            }
            builder.append(args[i]);
        }
        return builder.toString();
    }

    public void setLastLocation(Player player, Location pos) {
        this.playerLastLocation.put(player, pos);
    }

    public Location getLastLocation(Player player) {
        return this.playerLastLocation.get(player);
    }

    public boolean switchCanFly(Player player) {
        boolean canFly = !this.canFly(player);
        this.setCanFly(player, canFly);
        return canFly;
    }

    public boolean canFly(Player player) {
        return player.getAdventureSettings().canFly();
    }

    public void setCanFly(Player player, boolean canFly) {
        player.getAdventureSettings().setCanFly(canFly);
        player.getAdventureSettings().update();
    }

    public boolean switchVanish(Player player) {
        boolean vanished = this.isVanished(player);
        if (vanished) {
            this.setVanished(player, false);
            vanishedPlayers.remove(player);
        } else {
            this.setVanished(player, true);
            vanishedPlayers.add(player);
        }
        return !vanished;
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }

    public void setVanished(Player player, boolean vanished) {
        for (Player p : this.getServer().getInstance().getOnlinePlayers().values()) {
            if (vanished) {
                p.hidePlayer(player);
            } else {
                p.showPlayer(player);
            }
        }
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

    public void removeTPRequest(Player player) {
        for (Map.Entry<Integer, TPRequest> entry : this.tpRequests.entrySet()) {
            TPRequest request = entry.getValue();
            if (request.getFrom() == player || request.getTo() == player) {
                this.tpRequests.remove(entry.getKey());
            }
        }
    }

    public boolean setHome(Player player, String name, Location pos) {
        this.homeConfig.reload();
        Map<String, Object[]> map = (Map<String, Object[]>) this.homeConfig.get(player.getName().toLowerCase());
        if (map == null) {
            map = new HashMap<>();
        }
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
        return map != null && map.containsKey(name);
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
        List warp = this.warpConfig.getList(name);
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
        String[] list = this.warpConfig.getKeys().stream().toArray(String[]::new);
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public boolean isWarpExists(String name) {
        this.warpConfig.reload();
        return this.warpConfig.exists(name);
    }

    public Position getStandablePositionAt(Position pos) {
        int x = pos.getFloorX();
        int y = pos.getFloorY() + 1;
        int z = pos.getFloorZ();
        for (; y <= 128; y++) {
            if (!pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).isSolid() && !pos.level.getBlock(this.temporalVector.setComponents(x, y + 1, z)).isSolid()) {
                return new Position(x + 0.5, pos.level.getBlock(this.temporalVector.setComponents(x, y - 1, z)).getBoundingBox().maxY, z + 0.5, pos.level);
            }
        }
        return null;
    }

    public Position getHighestStandablePositionAt(Position pos) {
        int x = pos.getFloorX();
        int z = pos.getFloorZ();
        for (int y = 127; y >= 0; y--) {
            if (pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).isSolid()) {
                return new Position(x + 0.5, pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).getBoundingBox().maxY, z + 0.5, pos.level);
            }
        }
        return null;
    }

    //for peace
    public boolean mute(Player player, int d, int h, int m) {
        if (d < 0 || d > 30 || h < 0 || h >= 24 || m < 0 || m >= 60) return false;
        if (d == 30 && (h != 0 || m != 0)) return false;
        this.muteConfig.set(player.getName().toLowerCase(), System.currentTimeMillis() / 1000 + d * 86400 + h * 3600 + m * 60);
        this.muteConfig.save();
        return true;
    }

    public Integer getRemainingTimeToUnmute(Player player) {
        this.muteConfig.reload();
        Integer time = (Integer) this.muteConfig.get(player.getName().toLowerCase());
        return time == null ? null : (int) (time - System.currentTimeMillis() / 1000);
    }

    public boolean isMuted(Player player) {
        Integer time = this.getRemainingTimeToUnmute(player);
        if (time == null) return false;
        if (time <= 0) {
            this.unmute(player);
            return false;
        }
        return true;
    }

    public String getMuteTimeMessage(int d, int h, int m) {
        List<String> message = new ArrayList<>();
        if (d != 0) {
            message.add(d + " " + lang.translateString("commands.generic.day"));
        }
        if (h != 0) {
            message.add(h + " " + lang.translateString("commands.generic.hour"));
        }
        if (m != 0) {
            message.add(m + " " + lang.translateString("commands.generic.minute"));
        }
        return this.implode(" ", message.stream().toArray(String[]::new));
    }

    public String getUnmuteTimeMessage(Player player) {
        Integer time = this.getRemainingTimeToUnmute(player);
        if (time == null) return null;
        if (time / 86400d > 1) {
            return (int) Math.ceil(time / 86400d) + " " + lang.translateString("commands.generic.day");
        }
        if (time / 3600d > 1) {
            return (int) Math.ceil(time / 3600d) + " " + lang.translateString("commands.generic.hour");
        }
        return (int) Math.ceil(time / 60d) + " " + lang.translateString("commands.generic.minute");
    }

    public void unmute(Player player) {
        this.muteConfig.remove(player.getName().toLowerCase());
        this.muteConfig.save();
    }
}