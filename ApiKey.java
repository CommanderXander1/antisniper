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
    public static String key;

    public static void init() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        try {
            File dir = new File(Minecraft.getMinecraft().mcDataDir, "config/antisniper");

            if(!dir.exists()) {
                dir.mkdirs();
            }

            file = new File(dir, "settings.txt");
            if(file.createNewFile()) {
                key = null;
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                key = reader.readLine();
                reader.close();
            }
        } catch(Exception e) {
            System.out.println(e);
            player.addChatMessage(new ChatComponentText("Failed to create or open antisniper settings file for reason: " + e));
        }
    }

    public static void setKey(String key) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(key);
            writer.close();
            ApiKey.key = key;
            System.out.println("set api key to " + key);
            player.addChatMessage(new ChatComponentText( "Set api key to key ending in '" + key.substring(key.length() - 4) + "'"));
        } catch(Exception e) {
            System.out.println(e);
            player.addChatMessage(new ChatComponentText("Failed to set antisniper api-key for reason: " + e));
        }
    }
}
