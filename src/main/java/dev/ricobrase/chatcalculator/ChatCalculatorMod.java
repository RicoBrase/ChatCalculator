package dev.ricobrase.chatcalculator;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ChatCalculatorMod.MODID, version = ChatCalculatorMod.VERSION, name = ChatCalculatorMod.NAME, acceptedMinecraftVersions = ChatCalculatorMod.MC_VERSION)
public class ChatCalculatorMod {

    public static final String NAME = "ChatCalculator";
    public static final String MODID = "chatcalculator";
    public static final String VERSION = "1.3.0";
    public static final String MC_VERSION = "[1.12.2]";

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info(ChatCalculatorMod.NAME + " is initializing...");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

}
