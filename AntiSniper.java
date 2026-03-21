package com.antisniper;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = AntiSniper.MODID, version = AntiSniper.VERSION)
public class AntiSniper
{
    public static final String MODID = "antisniper";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new AntiSniperCommand());
        ApiKey.init();
    }
}