package dev.ricobrase.chatcalculator.events;

import dev.ricobrase.chatcalculator.TranslationMessages;
import dev.ricobrase.chatcalculator.Util;
import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Optional;

public class ClientChatEventListener {

    public static boolean onChatEventListener(String chatMessage) {
        // private calculation (=)
        if(chatMessage.matches("=[^=]*")) {
            return processPrivateCalculation(chatMessage);
        }

        return true;
    }

    public static String onChatModifyListener(String chatMessage) {

        // Double equals (==) escaping
        if(chatMessage.matches("={2}[^=]*")) {
            return chatMessage.substring(1);
        }

        return chatMessage;
    }

    private static boolean processPrivateCalculation(String chatMessage) {
        PlayerEntity sender = MinecraftClient.getInstance().player;

        if(sender == null) {
            return true;
        }

        String termToCalculate = chatMessage.substring(1);

        Optional<String> postfix = TermSolver.transformInfixToPostfix(termToCalculate);
        if(postfix.isEmpty()) {
            printTranslatedErrorMessage(sender, TranslationMessages.INVALID_CHARACTERS);
            return false;
        }

        try {
            double result = TermSolver.solvePostfix(postfix.get());
            String resultString = "= " + Util.convertDoubleToString(result);

            sender.sendMessage(Text.literal(termToCalculate));
            sender.sendMessage(Text.literal(resultString));
        }catch (NumberFormatException ex) {
            printTranslatedErrorMessage(sender, TranslationMessages.INVALID_CHARACTERS);
        }

        return false;
    }

    @SuppressWarnings("SameParameterValue")
    private static void printTranslatedErrorMessage(PlayerEntity player, TranslationMessages message) {
        Style redColor = Style.EMPTY.withColor(Formatting.RED);
        player.sendMessage(Text.translatable(message.getTranslationKey()).setStyle(redColor));
    }

}
