package com.sbplat.inventorysearcher.commands;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import com.sbplat.inventorysearcher.InventorySearcher;
import com.sbplat.inventorysearcher.utils.Utils;

public class CommandHighlightColor {
    public static String getCommandName() {
        return "highlightcolor";
    }

    public static List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("sethighlightcolor");
        return aliases;
    }

    public static void setHighlightColor(Color value) {
        InventorySearcher.INSTANCE.getConfig().setHighlightColor(value);
        Utils.displayModChatMessage("Highlight color set to 0x" + Integer.toHexString(value.getRGB()).substring(2).toUpperCase());
    }

    public static LiteralCommandNode<FabricClientCommandSource> register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(ClientCommandManager.literal(getCommandName())
            .then(ClientCommandManager.argument("value", StringArgumentType.string())
                .executes(context -> {
                    try {
                        setHighlightColor(Color.decode(StringArgumentType.getString(context, "value")));
                    } catch (NumberFormatException e) {
                        Utils.displayModChatMessage("Invalid hex color.");
                    }
                    return 1;
                })
            )
        );
    }
}
