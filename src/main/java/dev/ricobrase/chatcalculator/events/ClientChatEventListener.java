package dev.ricobrase.chatcalculator.events;

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
            Optional<String> postfix = TermSolver.transformInfixToPostfix(chatMessage.substring(1));
            if(postfix.isPresent()) {
                try {
                    double result = TermSolver.solvePostfix(postfix.get());
                    Minecraft.getInstance().player.sendMessage(new StringTextComponent(chatMessage.substring(1)), UUID.randomUUID());
                    if (result == Math.floor(result) && !Double.isInfinite(result) && result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                        result = Math.floor(result);
                        Minecraft.getInstance().player.sendMessage(new StringTextComponent(String.format("= %d", (int)result)), UUID.randomUUID());
                    }else{
                        Minecraft.getInstance().player.sendMessage(new StringTextComponent(String.format("= %f", result)), UUID.randomUUID());
                    }

                }catch (NumberFormatException ex) {
                    printInvalidCharactersMessage();
                }
            }else{
                printInvalidCharactersMessage();
            }
            event.setCanceled(true);
        }

    }

    private static void printInvalidCharactersMessage() {
        if(Minecraft.getInstance().player == null) return;

        Style redColor = Style.EMPTY.withColor(TextFormatting.RED);
        Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("chat.chatcalculator.invalidcharacters").setStyle(redColor), UUID.randomUUID());
    }

}
