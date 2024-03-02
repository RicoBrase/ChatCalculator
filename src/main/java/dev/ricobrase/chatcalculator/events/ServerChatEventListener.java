package dev.ricobrase.chatcalculator.events;

import dev.ricobrase.chatcalculator.TranslationMessages;
import dev.ricobrase.chatcalculator.Util;
import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Optional;

public class ServerChatEventListener {

    public static boolean allowChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters ignoredParams) {

        String chatMessage = message.getContent().getString();

        // global calculation (@=)
        if(chatMessage.matches("@=[^=]*")) {
            return processGlobalCalculation(chatMessage, sender);
        }

        return true;
    }



    private static boolean processGlobalCalculation(String chatMessage, ServerPlayerEntity sender) {

        String termToCalculate = chatMessage.substring(2);

        Optional<String> postfix = TermSolver.transformInfixToPostfix(termToCalculate);
        if(postfix.isPresent()) {
            try {
                double result = TermSolver.solvePostfix(postfix.get());
                sender.server.getPlayerManager().broadcast(Text.translatable(TranslationMessages.GLOBAL_CALC.getTranslationKey(), sender.getDisplayName(), termToCalculate), false);

                String resultString = String.format("= %s", Util.convertDoubleToString(result));
                sender.server.getPlayerManager().broadcast(Text.literal(resultString), false);

            }catch (NumberFormatException ex) {
                printTranslatedErrorMessage(sender, TranslationMessages.INVALID_CHARACTERS);
            }
        }else{
            printTranslatedErrorMessage(sender, TranslationMessages.INVALID_CHARACTERS);
        }

        return false;
    }

    @SuppressWarnings("SameParameterValue")
    private static void printTranslatedErrorMessage(ServerPlayerEntity player, TranslationMessages message) {
        Style redColor = Style.EMPTY.withColor(Formatting.RED);
        player.sendMessage(Text.translatable(message.getTranslationKey()).setStyle(redColor));
    }

}
