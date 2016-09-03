package cn.yescallop.essentialsnk;

import cn.nukkit.Player;

public class TPRequest {
    
    private long startTime;
    private Player from;
    private Player to;
    private boolean isTo;
    
    public TPRequest(long startTime, Player from, Player to, boolean isTo) {
        this.startTime = startTime;
        this.from = from;
        this.to = to;
        this.isTo = isTo;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public Player getFrom() {
        return from;
    }
    
    public Player getTo() {
        return to;
    }
    
    public boolean isTo() {
        return isTo;
    }
}