package com.antisniper;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import net.minecraft.util.EnumChatFormatting;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class GetStats {
    public static String FetchData(String player, String color) {
        if(ApiKey.hypixelKey == null) {
            return "Please set a hypixel API key before using stats, use '/as hypixelkey {key}' to do this";
        }

        // check blacklist
        String tag_string = "";
        if(ApiKey.urchinKey != null) {
            try {
                URL url = new URL("https://urchin.ws/player/" + player + "?key=" + ApiKey.urchinKey + "&sources=GAME");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                int status = conn.getResponseCode();

                if(status == HttpsURLConnection.HTTP_OK) {
                    StringBuilder sb = new StringBuilder();
                    Scanner scanner = new Scanner(conn.getInputStream());
                    while (scanner.hasNext()) {
                        sb.append(scanner.nextLine());
                    }

                    JSONObject json = new JSONObject(sb.toString());
                    System.out.println(sb.toString());
                    JSONArray tags = new JSONArray(json.getJSONArray("tags"));
                    for(int i = 0; i < tags.length(); i++) {
                        JSONObject tag = tags.getJSONObject(i);
                        tag_string += EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.BOLD + "[" + tag.getString("type").replace("_", " ") + "] ";
                    }
                } else {
                    System.out.println("Non-200 status getting blacklist for " + player + ": " + status);
                }
            } catch(Exception e) {
                System.out.println("Error querying blacklist: " + e);
            }
        }

        // get stats
        try {
            URL url = new URL("https://api.hypixel.net/player?key=" + ApiKey.hypixelKey + "&name=" + player);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();

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
                    player_stats = new PlayerStats(color + player, fKills, wins, beds, level, fDeaths, tag_string);
                } else {
                    player_stats = new PlayerStats(player, fKills, wins, beds, level, fDeaths, tag_string);
                }
                StatsCache.setPlayer(player, player_stats);

                return player_stats.toString();
            } else {
                // attempt to get stats from cache if the API request was denied
                String stats_string = StatsCache.getPlayer(player);
                if(stats_string != null) {
                    return stats_string;
                }
                return tag_string + EnumChatFormatting.AQUA + player + EnumChatFormatting.RESET + " - error getting data: non 200 response code";
            }
        } catch (JSONException e) {
            return tag_string + EnumChatFormatting.AQUA + player + EnumChatFormatting.RESET + " - player has no data";
        }
        catch (IOException e) {
            return EnumChatFormatting.AQUA + player + EnumChatFormatting.RESET + " - error getting data: " + e;
        }
    }
}
