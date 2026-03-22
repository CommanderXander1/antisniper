package com.antisniper;

import net.minecraft.util.EnumChatFormatting;

public class PlayerStats {
    String name;
    public float fkdr;
    public int finals;
    public int wins;
    public int beds;
    public int star;
    public String tags;

    public PlayerStats(String name, int finals, int wins, int beds, int star, int fDeaths, String tags) {
        this.name = name;
        // concat to 2 decimal places
        if(fDeaths == 0) {
            this.fkdr = finals;
        } else {
            this.fkdr = Math.round(((float) finals/ fDeaths) * 100f)/100f;
        }
        this.finals = finals;
        this.wins = wins;
        this.beds = beds;
        this.star = star;
        this.tags = tags;
    }

    public String toString() {
        return  tags + EnumChatFormatting.RESET + "[" + "\u272A" + EnumChatFormatting.DARK_GREEN + star + EnumChatFormatting.RESET + "] " + EnumChatFormatting.GRAY + name + EnumChatFormatting.RESET + " - " +
                EnumChatFormatting.GOLD + "fkdr: " + EnumChatFormatting.GREEN + fkdr +
                EnumChatFormatting.RESET + ", " + EnumChatFormatting.GOLD + "finals: " + EnumChatFormatting.GREEN + finals +
                EnumChatFormatting.RESET + ", " + EnumChatFormatting.GOLD + "wins: " + EnumChatFormatting.GREEN + wins +
                EnumChatFormatting.RESET + ", " + EnumChatFormatting.GOLD +  "beds: " + EnumChatFormatting.GREEN + beds;
    }
}
