package dev.ricobrase.chatcalculator.events;

import dev.ricobrase.chatcalculator.TranslationMessages;
import dev.ricobrase.chatcalculator.Util;
import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerChatEventListener {

    @SubscribeEvent
    public static void onServerChatEvent(ServerChatEvent event) {
        MinecraftServer server = event.getPlayer().getServer();

        if(server == null) {
            return;
        }

        String chatMessage = event.getMessage();
        ServerPlayerEntity player = event.getPlayer();

        if(chatMessage.matches("@=[^=]*")) {
            Optional<String> postfix = TermSolver.transformInfixToPostfix(chatMessage.substring(2));
            if(postfix.isPresent()) {
                try {
                    double result = TermSolver.solvePostfix(postfix.get());
                    server.getPlayerList().broadcastMessage(new TranslationTextComponent(TranslationMessages.GLOBAL_CALC.getTranslationKey(), player.getDisplayName(), chatMessage.substring(2)), ChatType.CHAT, UUID.randomUUID());

                    String resultString = String.format("= %s", Util.convertDoubleToString(result));
                    server.getPlayerList().broadcastMessage(new StringTextComponent(resultString), ChatType.CHAT, UUID.randomUUID());

                }catch (NumberFormatException ex) {
                    printTranslatedErrorMessage(player, TranslationMessages.INVALID_CHARACTERS);
                }
            }else{
                printTranslatedErrorMessage(player, TranslationMessages.INVALID_CHARACTERS);
            }
            event.setCanceled(true);
        }
    }

    private static void printTranslatedErrorMessage(ServerPlayerEntity player, TranslationMessages message) {
        Style redColor = Style.EMPTY.withColor(TextFormatting.RED);
        player.sendMessage(new TranslationTextComponent(message.getTranslationKey()).setStyle(redColor), UUID.randomUUID());
    }

}
