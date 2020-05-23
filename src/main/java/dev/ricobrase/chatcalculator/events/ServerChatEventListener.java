package dev.ricobrase.chatcalculator.events;

import dev.ricobrase.chatcalculator.termsolver.TermSolver;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Optional;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerChatEventListener {

    @SubscribeEvent
    public static void onServerChatEvent(ServerChatEvent event) {

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        String chatMessage = event.getMessage();
        EntityPlayerMP player = event.getPlayer();

        if(chatMessage.matches("@=[^=]*")) {
            Optional<String> postfix = TermSolver.transformInfixToPostfix(chatMessage.substring(2));
            if(postfix.isPresent()) {
                try {
                    double result = TermSolver.solvePostfix(postfix.get());
                    server.getPlayerList().sendMessage(new TextComponentTranslation("chat.chatcalculator.globalcalcmessage", player.getDisplayNameString(), chatMessage.substring(2)));
                    if (result == Math.floor(result) && !Double.isInfinite(result) && result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                        result = Math.floor(result);
                        server.getPlayerList().sendMessage(new TextComponentString(String.format("= %d", (int)result)));
                    }else{
                        server.getPlayerList().sendMessage(new TextComponentString(String.format("= %f", result)));
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

    private static void printInvalidCharactersMessage(EntityPlayerMP player) {
        Style redColor = (new Style()).setColor(TextFormatting.RED);
        player.sendMessage(new TextComponentTranslation("chat.chatcalculator.invalidcharacters").setStyle(redColor));
    }

}
