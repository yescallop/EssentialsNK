package cn.yescallop.essentialsnk;

import cn.nukkit.AdventureSettings;
import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
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
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;

import java.io.File;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class EssentialsAPI {

    public static final Integer[] NON_SOLID_BLOCKS = new Integer[]{Block.AIR, Block.SAPLING, Block.WATER, Block.STILL_WATER, Block.LAVA, Block.STILL_LAVA, Block.COBWEB, Block.TALL_GRASS, Block.BUSH, Block.DANDELION,
            Block.POPPY, Block.BROWN_MUSHROOM, Block.RED_MUSHROOM, Block.TORCH, Block.FIRE, Block.WHEAT_BLOCK, Block.SIGN_POST, Block.WALL_SIGN, Block.SUGARCANE_BLOCK,
            Block.PUMPKIN_STEM, Block.MELON_STEM, Block.VINE, Block.CARROT_BLOCK, Block.POTATO_BLOCK, Block.DOUBLE_PLANT};
    private static EssentialsAPI instance = null;
    private static Duration THIRTY_DAYS = Duration.ZERO.plusDays(30);
    private Vector3 temporalVector = new Vector3();
    private EssentialsNK plugin;
    private Map<Player, Location> playerLastLocation = new HashMap<>();
    private Map<Integer, TPRequest> tpRequests = new HashMap<>();
    private List<Player> vanishedPlayers = new ArrayList<>();
    private Config homeConfig;
    private Config warpConfig;
    private Config muteConfig;
    private Config ignoreConfig;

    public EssentialsAPI(EssentialsNK plugin) {
        instance = this;
        this.plugin = plugin;
        this.homeConfig = new Config(new File(plugin.getDataFolder(), "home.yml"), Config.YAML);
        this.warpConfig = new Config(new File(plugin.getDataFolder(), "warp.yml"), Config.YAML);
        this.muteConfig = new Config(new File(plugin.getDataFolder(), "mute.yml"), Config.YAML);
        this.ignoreConfig = new Config(new File(plugin.getDataFolder(), "ignore.yml"), Config.YAML);
    }

    public static EssentialsAPI getInstance() {
        return instance;
    }

    public Server getServer() {
        return plugin.getServer();
    }

    public PluginLogger getLogger() {
        return this.plugin.getLogger();
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
        return player.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT);
    }

    public void setCanFly(Player player, boolean canFly) {
        player.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, canFly);
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
        for (Player p : this.getServer().getOnlinePlayers().values()) {
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

    public boolean ignore(UUID player, UUID toIgnore) {
        this.ignoreConfig.reload();
        String playerId = player.toString();
        String toIgnoreId = toIgnore.toString();
        Map<String, Object> ignores = this.ignoreConfig.get(playerId, new HashMap<>());
        boolean add = !ignores.containsKey(toIgnoreId);
        if (add) {
            ignores.put(toIgnoreId, null);
        } else {
            ignores.remove(toIgnoreId);
        }
        this.ignoreConfig.set(playerId, ignores);
        this.ignoreConfig.save();
        return add;
    }

    public boolean isIgnoring(UUID player, UUID target) {
        String playerId = player.toString();
        String targetId = target.toString();

        Map<String, Object> ignores = this.ignoreConfig.get(playerId, null);

        return ignores != null && ignores.containsKey(targetId);
    }

    public boolean setHome(Player player, String name, Location pos) {
        this.homeConfig.reload();
        Map<String, Object> map = this.homeConfig.get(player.getName().toLowerCase(), new HashMap<>());

        boolean replaced = map.containsKey(name);
        Object[] home = new Object[]{pos.level.getName(), pos.x, pos.y, pos.z, pos.yaw, pos.pitch};
        map.put(name, home);
        this.homeConfig.set(player.getName().toLowerCase(), map);
        this.homeConfig.save();
        return replaced;
    }

    public Location getHome(Player player, String name) {
        this.homeConfig.reload();
        Map<String, ArrayList<Object>> map = this.homeConfig.get(player.getName().toLowerCase(), null);
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
        Map<String, Object> map = this.homeConfig.get(player.getName().toLowerCase(), null);
        if (map == null) {
            return;
        }
        map.remove(name);
        this.homeConfig.set(player.getName().toLowerCase(), map);
        this.homeConfig.save();
    }

    public String[] getHomesList(Player player) {
        this.homeConfig.reload();
        Map<String, Object> map = this.homeConfig.get(player.getName().toLowerCase(), null);
        if (map == null) {
            return new String[]{};
        }
        String[] list = map.keySet().toArray(new String[0]);
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public boolean isHomeExists(Player player, String name) {
        this.homeConfig.reload();
        Map<String, Object> map = this.homeConfig.get(player.getName().toLowerCase(), null);
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
        String[] list = this.warpConfig.getKeys().toArray(new String[0]);
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
                return new Position(x + 0.5, pos.level.getBlock(this.temporalVector.setComponents(x, y - 1, z)).getBoundingBox().getMaxY(), z + 0.5, pos.level);
            }
        }
        return null;
    }

    public Position getHighestStandablePositionAt(Position pos) {
        int x = pos.getFloorX();
        int z = pos.getFloorZ();
        for (int y = 127; y >= 0; y--) {
            if (pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).isSolid()) {
                return new Position(x + 0.5, pos.level.getBlock(this.temporalVector.setComponents(x, y, z)).getBoundingBox().getMaxY(), z + 0.5, pos.level);
            }
        }
        return null;
    }

    //for peace
    public boolean mute(Player player, int d, int h, int m, int s) {
        return this.mute(player, Duration.ZERO.plusDays(d).plusHours(h).plusMinutes(m).plusSeconds(s));
    }

    //for peace too -- lmlstarqaq
    public boolean mute(Player player, Duration t) {
        if (t.isNegative() || t.isZero()) return false;
        // t>30 => (t!=30 && t>=30) => (t!=30 && t-30>=0) => (t!=30 && !(t-30<0))
        if (t.toDays() != 30 && !(t.minus(THIRTY_DAYS).isNegative())) return false; // t>30
        this.muteConfig.set(player.getName().toLowerCase(), Timestamp.valueOf(LocalDateTime.now().plus(t)).getTime() / 1000);
        this.muteConfig.save();
        return true;
    }

    public Integer getRemainingTimeToUnmute(Player player) {
        this.muteConfig.reload();
        Integer time = (Integer) this.muteConfig.get(player.getName().toLowerCase());
        return time == null ? null : (int) (time - Timestamp.valueOf(LocalDateTime.now()).getTime() / 1000);
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

    public String getMuteTimeMessage(int d, int h, int m, int s) {
        return getDurationString(Duration.ZERO.plusDays(d).plusHours(h).plusMinutes(m).plusSeconds(s));
    }

    public String getUnmuteTimeMessage(Player player) {
        Integer time = this.getRemainingTimeToUnmute(player);
        return getDurationString(Duration.ofSeconds(time));
    }

    public void unmute(Player player) {
        this.muteConfig.remove(player.getName().toLowerCase());
        this.muteConfig.save();
    }

    // Scallop: Thanks lmlstarqaq
    // %0 days %1 hours %2 minutes %3 seconds, language localized.
    public String getDurationString(Duration duration) {
        if (duration == null) return "null";
        long d = duration.toDays();
        long h = duration.toHours() % 24;
        long m = duration.toMinutes() % 60;
        long s = duration.getSeconds() % 60;
        String d1 = "", h1 = "", m1 = "", s1 = "";
        //Singulars and plurals. Maybe necessary for English or other languages. 虽然中文似乎没有名词的单复数 -- lmlstarqaq
        if (d > 1) d1 = Language.translate("commands.generic.days", d);
        else if (d > 0) d1 = Language.translate("commands.generic.day", d);
        if (h > 1) h1 = Language.translate("commands.generic.hours", h);
        else if (h > 0) h1 = Language.translate("commands.generic.hour", h);
        if (m > 1) m1 = Language.translate("commands.generic.minutes", m);
        else if (m > 0) m1 = Language.translate("commands.generic.minute", m);
        if (s > 1) s1 = Language.translate("commands.generic.seconds", s);
        else if (s > 0) s1 = Language.translate("commands.generic.second", s);
        //In some languages, times are read from SECONDS to HOURS, which should be noticed.
        return Language.translate("commands.generic.time.format", d1, h1, m1, s1).trim().replaceAll(" +", " ");
    }
}