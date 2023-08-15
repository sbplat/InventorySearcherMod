package com.sbplat.inventorysearcher.utils;

import java.text.MessageFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import com.sbplat.inventorysearcher.InventorySearcher;

public class Utils {
    public static void displayRawChatMessage(String message) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.sendSystemMessage(Component.literal(message));
        }
    }

    public static void displayModChatMessage(String message) {
        displayRawChatMessage(MessageFormat.format("\u00A7a[\u00A72InventorySearcher\u00A7a] \u00A7e{0}", message));
    }
}
