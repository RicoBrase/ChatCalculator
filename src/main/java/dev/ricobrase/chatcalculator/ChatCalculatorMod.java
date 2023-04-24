package dev.ricobrase.chatcalculator;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ChatCalculatorMod.MODID)
public class ChatCalculatorMod {

    public static final String NAME = "ChatCalculator";
    public static final String MODID = "chatcalculator";
    public static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("unused")
    private void setup(final FMLCommonSetupEvent ignoredEvent) {
        LOGGER.info(ChatCalculatorMod.NAME + " is initializing...");
    }

}
