package dev.ricobrase.chatcalculator.events;

import dev.ricobrase.chatcalculator.TranslationMessages;
import dev.ricobrase.chatcalculator.Util;
import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientChatEventListener {

    private static String lastResult = null;

    @SubscribeEvent
    public static void onClientChatEvent(final ClientChatEvent event) {

        String chatMessage = event.getMessage();

        if(chatMessage.matches("={2}[^=]*")) {
            event.setMessage(chatMessage.substring(1));
            return;
        }

        if(Minecraft.getInstance().player == null) {
            return;
        }

        if(chatMessage.matches("=[^=]*")) {
            String termToCalculate = chatMessage.substring(1);

            if(termToCalculate.contains("$0")) {
                if (lastResult == null) {
                    printTranslatedErrorMessage(TranslationMessages.NO_PREVIOUS_RESULT);
                    event.setCanceled(true);
                    return;
                }

                termToCalculate = termToCalculate.replace("$0", lastResult.replace(',', '.'));
            }

            Optional<String> postfix = TermSolver.transformInfixToPostfix(termToCalculate);
            if(postfix.isPresent()) {
                try {
                    double result = TermSolver.solvePostfix(postfix.get());
                    lastResult = Util.convertDoubleToString(result);

                    Minecraft.getInstance().player.sendMessage(new StringTextComponent(termToCalculate), UUID.randomUUID());

                    String resultString = String.format("= %s", Util.convertDoubleToString(result));
                    Minecraft.getInstance().player.sendMessage(new StringTextComponent(resultString), UUID.randomUUID());

                }catch (NumberFormatException ex) {
                    printTranslatedErrorMessage(TranslationMessages.INVALID_CHARACTERS);
                }
            }else{
                printTranslatedErrorMessage(TranslationMessages.INVALID_CHARACTERS);
            }
            event.setCanceled(true);
        }

    }

    private static void printTranslatedErrorMessage(TranslationMessages message) {
        if(Minecraft.getInstance().player == null) return;

        Style redColor = Style.EMPTY.withColor(TextFormatting.RED);
        Minecraft.getInstance().player.sendMessage(new TranslationTextComponent(message.getTranslationKey()).setStyle(redColor), UUID.randomUUID());
    }



}
