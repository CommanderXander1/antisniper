package com.antisniper;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import net.minecraft.util.EnumChatFormatting;
import org.json.JSONException;
import org.json.JSONObject;

public class GetStats {
    public static String FetchData(String player, String color) {
        if(ApiKey.key == null) {
            return "Please set API key before using stats, use '/as apikey {key}' to do this";
        }
        try {
            URL url = new URL("https://api.hypixel.net/player?key=" + ApiKey.key + "&name=" + player);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            System.out.println("Response code: " + status);

            if(status == HttpsURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while(scanner.hasNext()) {
                    sb.append(scanner.nextLine());
                }

                JSONObject json = new JSONObject(sb.toString());

                JSONObject playerData = json.getJSONObject("player");
                JSONObject achievements = playerData.getJSONObject("achievements");
                JSONObject stats = playerData.getJSONObject("stats");
                JSONObject bw_stats = stats.getJSONObject("Bedwars");

                int fKills;
                int wins;
                int beds;
                int level;
                int fDeaths;

                try {
                    fKills = bw_stats.getInt("final_kills_bedwars");
                } catch(Exception e) {
                    fKills = 0;
                }

                try {
                    wins = bw_stats.getInt("wins_bedwars");
                } catch(Exception e) {
                    wins = 0;
                }

                try {
                    beds = bw_stats.getInt("beds_broken_bedwars");
                } catch(Exception e) {
                    beds = 0;
                }

                try {
                    level = achievements.getInt("bedwars_level");
                } catch(Exception e) {
                    level = 0;
                }

                try {
                    fDeaths = bw_stats.getInt("final_deaths_bedwars");
                } catch (Exception e) {
                    fDeaths = 0;
                }

                PlayerStats player_stats;
                if(color != null) {
                    player_stats = new PlayerStats(color + player, fKills, wins, beds, level, fDeaths);
                } else {
                    player_stats = new PlayerStats(player, fKills, wins, beds, level, fDeaths);
                }
                StatsCache.setPlayer(player, player_stats);

                return player_stats.toString();
            } else {
                // attempt to get stats from cache if the API request was denied
                String stats_string = StatsCache.getPlayer(player);
                if(stats_string != null) {
                    return stats_string;
                }
                return EnumChatFormatting.AQUA + player + EnumChatFormatting.RESET + " - error getting data: non 200 response code";
            }
        } catch (JSONException e) {
            return EnumChatFormatting.AQUA + player + EnumChatFormatting.RESET + " - player has no data";
        }
        catch (IOException e) {
            return EnumChatFormatting.AQUA + player + EnumChatFormatting.RESET + " - error getting data: " + e;
        }
    }
}
