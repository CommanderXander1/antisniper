package com.antisniper;

import java.util.HashMap;

public class StatsCache {
    public static HashMap<String, PlayerStats> Cache = new HashMap<String, PlayerStats>();

    public static void setPlayer(String player, PlayerStats stats) {
        StatsCache.Cache.put(player.toLowerCase(), stats);
    }

    public static String getPlayer(String player) {
        PlayerStats stats_string = StatsCache.Cache.get(player.toLowerCase());
        if(stats_string == null) {
            return null;
        }
        return StatsCache.Cache.get(player.toLowerCase()).toString();
    }
}
