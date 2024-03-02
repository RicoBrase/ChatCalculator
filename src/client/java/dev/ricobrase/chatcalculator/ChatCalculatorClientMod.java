package dev.ricobrase.chatcalculator;

import dev.ricobrase.chatcalculator.events.ClientChatEventListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

public class ChatCalculatorClientMod implements ClientModInitializer
{

    @Override
    public void onInitializeClient() {
        ChatCalculatorMod.LOGGER.info(ChatCalculatorMod.NAME + " - client code is initializing...");
        ClientSendMessageEvents.ALLOW_CHAT.register(ClientChatEventListener::onChatEventListener);
        ClientSendMessageEvents.MODIFY_CHAT.register(ClientChatEventListener::onChatModifyListener);
    }
}
