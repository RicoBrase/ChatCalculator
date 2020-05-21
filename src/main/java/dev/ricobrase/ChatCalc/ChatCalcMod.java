package dev.ricobrase.ChatCalc;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ChatCalcMod.MODID, version = ChatCalcMod.VERSION, name = ChatCalcMod.NAME, acceptedMinecraftVersions = ChatCalcMod.MC_VERSION)
public class ChatCalcMod {

    public static final String NAME = "ChatCalc";
    public static final String MODID = "chatcalc";
    public static final String VERSION = "1.1.0";
    public static final String MC_VERSION = "[1.12.2]";

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info(ChatCalcMod.NAME + " is initializing...");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

}
