package com.antisniper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ApiKey {
    public static File file;
    public static String hypixelKey;
    public static String urchinKey;

    public static void init() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        try {
            File dir = new File(Minecraft.getMinecraft().mcDataDir, "config/antisniper");

            if(!dir.exists()) {
                dir.mkdirs();
            }

            file = new File(dir, "settings.txt");
            if(file.createNewFile()) {
                hypixelKey = null;
                urchinKey = null;
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                hypixelKey = reader.readLine();
                urchinKey = reader.readLine();
                reader.close();
            }
        } catch(Exception e) {
            System.out.println(e);
            player.addChatMessage(new ChatComponentText("Failed to create or open antisniper settings file for reason: " + e));
        }
    }

    public static void setHypixelKey(String key) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(key + "\n" + urchinKey);
            writer.close();
            ApiKey.hypixelKey = key;
            System.out.println("set hypixel api key to " + key);
            player.addChatMessage(new ChatComponentText( "Set hypixel api key to key ending in '" + key.substring(key.length() - 4) + "'"));
        } catch(Exception e) {
            System.out.println(e);
            player.addChatMessage(new ChatComponentText("Failed to set hypixel api key for reason: " + e));
        }
    }

    public static void setUrchinKey(String key) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(hypixelKey + "\n" + key);
            writer.close();
            ApiKey.urchinKey = key;
            System.out.println("set urchin api key to " + key);
            player.addChatMessage(new ChatComponentText( "Set urchin api key to key ending in '" + key.substring(key.length() - 4) + "'"));
        } catch(Exception e) {
            System.out.println(e);
            player.addChatMessage(new ChatComponentText("Failed to set urchin api key for reason: " + e));
        }
    }
}
