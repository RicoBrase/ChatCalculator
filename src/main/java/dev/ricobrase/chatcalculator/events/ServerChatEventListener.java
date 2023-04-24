package dev.ricobrase.chatcalculator.events;

import dev.ricobrase.chatcalculator.TranslationMessages;
import dev.ricobrase.chatcalculator.Util;
import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerChatEventListener {

    @SubscribeEvent
    public static void onServerChatEvent(ServerChatEvent event) {
        MinecraftServer server = event.getPlayer().getServer();

        if(server == null) {
            return;
        }

        String chatMessage = event.getMessage().getString();
        ServerPlayer player = event.getPlayer();

        if(chatMessage.matches("@=[^=]*")) {
            Optional<String> postfix = TermSolver.transformInfixToPostfix(chatMessage.substring(2));
            if(postfix.isPresent()) {
                try {
                    double result = TermSolver.solvePostfix(postfix.get());
                    server.getPlayerList().broadcastSystemMessage(Component.translatable(TranslationMessages.GLOBAL_CALC.getTranslationKey(), player.getDisplayName(), chatMessage.substring(2)), false);

                    String resultString = String.format("= %s", Util.convertDoubleToString(result));
                    server.getPlayerList().broadcastSystemMessage(Component.literal(resultString), false);

                }catch (NumberFormatException ex) {
                    printTranslatedErrorMessage(player, TranslationMessages.INVALID_CHARACTERS);
                }
            }else{
                printTranslatedErrorMessage(player, TranslationMessages.INVALID_CHARACTERS);
            }
            event.setCanceled(true);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static void printTranslatedErrorMessage(@NotNull ServerPlayer player, @NotNull TranslationMessages message) {
        Style redColor = Style.EMPTY.withColor(ChatFormatting.RED);
        player.sendSystemMessage(Component.translatable(message.getTranslationKey(), redColor));
    }

}
