package dev.ricobrase.chatcalculator.events;

import dev.ricobrase.chatcalculator.TranslationMessages;
import dev.ricobrase.chatcalculator.Util;
import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientChatEventListener {

    @SubscribeEvent
    public static void onClientChatEvent(final ClientChatEvent event) {

        String chatMessage = event.getMessage();

        // Double equals (==) escaping
        if(chatMessage.matches("={2}[^=]*")) {
            event.originalMessage = chatMessage.substring(1);
            return;
        }

        if(!chatMessage.matches("=[^=]*")) {
            return;
        }

        processChatCalculationMessage(event);
    }

    private static void processChatCalculationMessage(final ClientChatEvent clientChatEvent) {
        if(Minecraft.getInstance().player == null) {
            return;
        }

        String termToCalculate = clientChatEvent.getMessage().substring(1);

        Optional<String> postfix = TermSolver.transformInfixToPostfix(termToCalculate);
        if(postfix.isEmpty()) {
            printTranslatedErrorMessage(TranslationMessages.INVALID_CHARACTERS);
            clientChatEvent.setCanceled(true);
            return;
        }

        try {
            double result = TermSolver.solvePostfix(postfix.get());

            Minecraft.getInstance().player.sendSystemMessage(Component.literal(termToCalculate));

            String resultString = String.format("= %s", Util.convertDoubleToString(result));
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(resultString));

        }catch (NumberFormatException ex) {
            printTranslatedErrorMessage(TranslationMessages.INVALID_CHARACTERS);
        }

        clientChatEvent.setCanceled(true);
    }

    private static void printTranslatedErrorMessage(TranslationMessages message) {
        if(Minecraft.getInstance().player == null) return;

        Style redColor = Style.EMPTY.withColor(ChatFormatting.RED);
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable(message.getTranslationKey()).setStyle(redColor));
    }



}
