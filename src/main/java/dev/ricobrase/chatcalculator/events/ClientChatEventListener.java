package dev.ricobrase.chatcalculator.events;

import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Optional;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientChatEventListener {

    @SubscribeEvent
    public static void onClientChatEvent(ClientChatEvent event) {

        String chatMessage = event.getMessage();

        // Client only
        if(chatMessage.startsWith("=")) {
            Optional<String> postfix = TermSolver.transformInfixToPostfix(chatMessage.substring(1));
            if(postfix.isPresent()) {
                try {
                    double result = TermSolver.solvePostfix(postfix.get());
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(chatMessage.substring(1)));
                    if (result == Math.floor(result) && !Double.isInfinite(result) && result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                        result = Math.floor(result);
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("= %d", (int)result)));
                    }else{
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("= %f", result)));
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
        Style redColor = (new Style()).setColor(TextFormatting.RED);
        Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("chat.chatcalculator.invalidcharacters").setStyle(redColor));
    }

}
