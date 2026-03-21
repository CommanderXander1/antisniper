package com.antisniper;

import java.util.HashMap;

public class StatsCache {
    public static HashMap<String, PlayerStats> Cache;

    public static void setPlayer(String player, PlayerStats stats) {
        Cache.put(player.toLowerCase(), stats);
    }
}
