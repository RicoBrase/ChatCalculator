package dev.ricobrase.chatcalculator.events;

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
                    server.getPlayerList().broadcastMessage(new TranslationTextComponent("chat.chatcalculator.globalcalcmessage", player.getDisplayName(), chatMessage.substring(2)), ChatType.CHAT, UUID.randomUUID());
                    if (result == Math.floor(result) && !Double.isInfinite(result) && result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                        result = Math.floor(result);
                        server.getPlayerList().broadcastMessage(new StringTextComponent(String.format("= %d", (int)result)), ChatType.CHAT, UUID.randomUUID());
                    }else{
                        server.getPlayerList().broadcastMessage(new StringTextComponent(String.format("= %f", result)), ChatType.CHAT, UUID.randomUUID());
                    }

                }catch (NumberFormatException ex) {
                    printInvalidCharactersMessage(player);
                }
            }else{
                printInvalidCharactersMessage(player);
            }
            event.setCanceled(true);
        }
    }

    private static void printInvalidCharactersMessage(ServerPlayerEntity player) {
        Style redColor = Style.EMPTY.withColor(TextFormatting.RED);
        player.sendMessage(new TranslationTextComponent("chat.chatcalculator.invalidcharacters").setStyle(redColor), UUID.randomUUID());
    }

}
