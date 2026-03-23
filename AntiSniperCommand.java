package com.antisniper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class AntiSniperCommand extends CommandBase {
    @Override
    public String getCommandName() { return "as"; }

    @Override
    public String getCommandUsage(ICommandSender sender) { return "/as <stats|hypixelkey|urchinkey>"; }

    @Override
    public void processCommand(ICommandSender sender, final String[] args) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if(args.length > 0 && args[0].equals("stats")) {
            player.addChatMessage(new ChatComponentText( EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.BOLD + "[Antisniper]" + EnumChatFormatting.RESET + EnumChatFormatting.GREEN + " fetching player data"));

            if(args.length > 1) {
                // get stats of players in argument asynchronously
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 1; i < args.length; i++) {
                            player.addChatMessage(new ChatComponentText(GetStats.FetchData(args[i], null)));
                        }
                    }
                };
                Thread thread = new Thread(task);
                thread.start();
            }

            else {
                // get stats of every player in lobby asynchronously
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, ArrayList<String>> teams = new HashMap<String, ArrayList<String>>();
                        ArrayList<String> not_on_team = new ArrayList<String>();

                        // make a copy of the list to iterate over otherwise it throws ConcurrentModificationException
                        Collection<NetworkPlayerInfo> tab_list = new ArrayList<NetworkPlayerInfo>(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap());

                        for(NetworkPlayerInfo p : tab_list) {
                            String name = p.getGameProfile().getName();
                            ScorePlayerTeam team = p.getPlayerTeam();

                            if(team != null) {
                                String stats_string = GetStats.FetchData(name, team.getColorPrefix());

                                if(teams.containsKey(team.getColorPrefix())) {
                                    teams.get(team.getColorPrefix()).add(stats_string);
                                } else {
                                    teams.put(team.getColorPrefix(), new ArrayList<String>(Arrays.asList(stats_string)));
                                }
                            } else {
                                not_on_team.add(GetStats.FetchData(name, null));
                            }
                        }

                        // iterate through hashmap so that team members are grouped together
                        for(ArrayList<String> stats_list : teams.values()) {
                            for(String stats : stats_list) {
                                player.addChatMessage(new ChatComponentText(stats));
                            }
                        }

                        for(String stats : not_on_team) {
                            player.addChatMessage(new ChatComponentText(stats));
                        }
                    }
                };
                Thread thread = new Thread(task);
                thread.start();
            }
        } else if (args.length > 0 && args[0].toLowerCase().equals("hypixelkey")) {
            if(args.length == 2) {
                if(args[1].length() > 30 && args[1].length() < 50) {
                    ApiKey.setHypixelKey(args[1]);
                } else {
                    player.addChatMessage(new ChatComponentText("Please provide a valid key (30-50 characters)"));
                }
            } else {
                player.addChatMessage(new ChatComponentText( "Please provide a single argument for '/as hypixelkey {api-key}'"));
            }
        } else if (args.length > 0 && args[0].toLowerCase().equals("urchinkey")) {
            if(args.length == 2) {
                if(args[1].length() > 30 && args[1].length() < 50) {
                    ApiKey.setUrchinKey(args[1]);
                } else {
                    player.addChatMessage(new ChatComponentText("Please provide a valid key (30-50 characters)"));
                }
            } else {
                player.addChatMessage(new ChatComponentText( "Please provide a single argument for '/as urchinkey {api-key}'"));
            }
        } else {
            player.addChatMessage(new ChatComponentText( "Please provide an argument. Valid usage follows '/as <stats|hypixelkey|urchinkey>'"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }
}
