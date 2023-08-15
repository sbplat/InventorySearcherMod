package com.sbplat.inventorysearcher.commands;

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

public class CommandHighlightAlpha {
    public static String getCommandName() {
        return "highlightalpha";
    }

    public static List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("sethighlightalpha");
        return aliases;
    }

    public static void setHighlightAlpha(int value) {
        InventorySearcher.INSTANCE.getConfig().setHighlightAlpha(value);
        Utils.displayModChatMessage("Highlight alpha set to " + value);
    }

    public static LiteralCommandNode<FabricClientCommandSource> register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(ClientCommandManager.literal(getCommandName())
            .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0, 255))
                .executes(context -> {
                    setHighlightAlpha(IntegerArgumentType.getInteger(context, "value"));
                    return 1;
                })
            )
        );
    }
}
