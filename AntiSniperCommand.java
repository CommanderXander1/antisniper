package com.antisniper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class AntiSniperCommand extends CommandBase {
    @Override
    public String getCommandName() { return "as"; }

    @Override
    public String getCommandUsage(ICommandSender sender) { return "/as <stats|apikey>"; }

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
                            try {
                                player.addChatMessage(new ChatComponentText(GetStats.FetchData(args[i])));
                            } catch(Exception e) {
                                player.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + args[i] + EnumChatFormatting.RESET + " - error fetching data: " + e));
                            }
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
                        for(EntityPlayer p : Minecraft.getMinecraft().theWorld.playerEntities) {
                            String name = p.getDisplayNameString();
                            try {
                                player.addChatMessage(new ChatComponentText(GetStats.FetchData(name)));
                            } catch(Exception e) {
                                player.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + name + EnumChatFormatting.RESET + " - error fetching data: " + e));
                            }
                        }
                    }
                };
                Thread thread = new Thread(task);
                thread.start();
            }
        } else if (args.length > 0 && args[0].equals("apikey")) {
            if(args.length == 2) {
                if(args[1].length() == 36) {
                    ApiKey.setKey(args[1]);
                } else {
                    player.addChatMessage(new ChatComponentText("Please provide a valid key (36 characters)"));
                }
            } else {
                player.addChatMessage(new ChatComponentText( "Please provide a single argument for '/as apikey {api-key}'"));
            }
        } else {
            player.addChatMessage(new ChatComponentText( "Please provide an argument. Valid usage follows '/as <stats|apikey>'"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }
}
