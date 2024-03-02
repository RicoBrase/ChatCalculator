package dev.ricobrase.chatcalculator;

import dev.ricobrase.chatcalculator.events.ServerChatEventListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatCalculatorMod implements ModInitializer {

    public static final String NAME = "ChatCalculator";
    public static final String MODID = "chatcalculator";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        LOGGER.info(NAME + " - common code is initializing...");
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ServerChatEventListener::allowChatMessage);
    }

}
